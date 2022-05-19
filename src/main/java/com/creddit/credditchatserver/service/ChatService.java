package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.dto.ChatRoomDto;
import com.creddit.credditchatserver.dto.ChatRoomIdDto;
import com.creddit.credditchatserver.dto.ChatRoomRegisterDto;
import com.creddit.credditchatserver.dto.ProfileResponseDto;
import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.exception.user.UserException;
import com.creddit.credditchatserver.exception.user.UserExceptionType;
//import com.creddit.credditchatserver.repository.ChatRoomRepository;
import com.creddit.credditchatserver.trace.Trace;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ChatService {

    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    private RedisTemplate<String, User> userTemplate;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Trace
    public ChatRoom leftChatRoom(String userName, ChatRoomIdDto chatRoomId) {
        HashOperations<String, String, ChatRoom> hashOperations = chatRoomRedisTemplate.opsForHash();
        Map<String, ChatRoom> mapper = hashOperations.entries(userName);
        ChatRoom chatRoom = mapper.values().stream().filter(s -> s.getId().equals(chatRoomId.getChatRoomId())).findFirst().get();
        hashOperations.delete(userName, chatRoomId.getChatRoomId());


        if(!chatRoom.getTarget().equals(userName)) {
            ChatRoom chatRoomOtherUser = mapper.values().stream().filter(s -> s.getId().equals(chatRoomId.getChatRoomId())).findFirst().get();
            List<String> leftUsers = chatRoomOtherUser.getLeftUsers();
            ProfileResponseDto otherUser = chatRoomOtherUser.getUsers().stream().filter(s -> !s.getNickname().equals(userName)).findFirst().get();
            hashOperations.delete(otherUser.getNickname(), chatRoomId.getChatRoomId());
            leftUsers.add(userName);
            chatRoomOtherUser.setTarget(userName);
            chatRoomOtherUser.setLeftUsers(leftUsers);
            LocalDateTime currentDateTime = LocalDateTime.now();

            Message message = new Message(
                    "CHAT_MANAGER",
                    "CHAT_MANAGER", chatRoomId.getChatRoomId(),
                    userName + "님이 나가셨습니다",
                    currentDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"))

            );
            chatRoomOtherUser.getMessages().add(message);

            hashOperations.put(otherUser.getNickname(), chatRoomId.getChatRoomId(), chatRoomOtherUser);
            simpMessagingTemplate.convertAndSend("/topic/" + message.getChatRoomId(), message);
        }
        return chatRoom;
    }

    @Trace
    public void createChatMessage(Message message) throws JsonProcessingException {
        HashOperations<String, String, ChatRoom> chatRoomMaps = chatRoomRedisTemplate.opsForHash();
        String sender = message.getSender();
        String receiver = message.getReceiver();
        String chatRoomId = message.getChatRoomId();

        boolean hasSenderMessages = hasMessages(chatRoomMaps, sender, receiver, chatRoomId);
        boolean hasReceiverMessages = hasMessages(chatRoomMaps, receiver, sender, chatRoomId);
        if (!sender.equals(receiver)) {
            getChatRoomAndUpdateMessage(message, chatRoomMaps, sender, receiver, chatRoomId, hasSenderMessages, "SENDER");
            getChatRoomAndUpdateMessage(message, chatRoomMaps, receiver, sender, chatRoomId, hasReceiverMessages, "RECEIVER");
        } else {
            getChatRoomAndUpdateMessage(message, chatRoomMaps, sender, receiver, chatRoomId, hasSenderMessages, "SENDER");
        }

    }
    @Trace
    private ProfileResponseDto getProfileInformation(String name) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String API_URL = "http://localhost:8080/profile/show/"+name;
        List<ProfileResponseDto> profiles = new ArrayList<>();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("", httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        ProfileResponseDto dto = objectMapper.readValue(responseEntity.getBody(), ProfileResponseDto.class);

        return dto;
    }

    private void getChatRoomAndUpdateMessage(
            Message message,
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            String myId,
            String userId,
            String chatRoomId,
            boolean hasMessages,
            String messageType
    ) throws JsonProcessingException {
        ProfileResponseDto myProfiles = getProfileInformation(myId);
        ProfileResponseDto userProfiles = getProfileInformation(userId);
        ChatRoom chatRoom = hasMessages == true ? chatRoomMaps.get(myId, chatRoomId) : new ChatRoom(
                chatRoomId,
                userId,
                new ArrayList<>(Arrays.asList(myProfiles, userProfiles)),
                new ArrayList<>(),
                new ArrayList<>()
        );
        updateChatRoomAndMessages(chatRoomMaps, chatRoom, message, messageType);
    }
    private boolean hasMessages(
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            String myId,
            String userId,
            String chatRoomId) {
        return chatRoomMaps.hasKey(myId, chatRoomId);
    }

    public void createChatRoom(ChatRoomRegisterDto chatRoomRegisterDto) throws Exception{
        HashOperations<String, String, ChatRoom> chatRoomMaps = chatRoomRedisTemplate.opsForHash();

//        if(chatRoomMaps.hasKey(myId, userId)){
//            throw new UserException(UserExceptionType.ALREADY_EXIST_CHAT_USER);
//        }
        ProfileResponseDto myProfiles = getProfileInformation(chatRoomRegisterDto.getMyId());
        ProfileResponseDto userProfiles = getProfileInformation(chatRoomRegisterDto.getUserId());

        List<ProfileResponseDto> profiles = new ArrayList();
        profiles.add(myProfiles);
        profiles.add(userProfiles);
        String chatRoomId = UUID.randomUUID().toString();

        chatRoomMaps.put(chatRoomRegisterDto.getMyId(),chatRoomId, new ChatRoom(
                chatRoomId,
                chatRoomRegisterDto.getUserId(),
                profiles,
                new ArrayList<>(),
                new ArrayList<>()));
    }

    public Collection<ChatRoom> getChatRooms(String userName) throws JsonProcessingException {
        HashOperations<String, String, ChatRoom> hashOperations = chatRoomRedisTemplate.opsForHash();
        Map<String, ChatRoom> mapper = hashOperations.entries(userName);

        return mapper.values();
    }

    @Trace
    private void updateChatRoomAndMessages(
            HashOperations<String, String,  ChatRoom> chatRoomMaps,
            ChatRoom chatRoom,
            Message message,
            String messageType) {
        chatRoom.getMessages().add(message);
        if (message.getSender().equals(message.getReceiver())) {
            chatRoomMaps.put(message.getSender(), chatRoom.getId(), chatRoom);
        }
        else {
            if (Objects.equals(messageType, "SENDER")) {
                chatRoomMaps.put(message.getSender(), chatRoom.getId(), chatRoom);
            } else {
                chatRoomMaps.put(message.getReceiver(), chatRoom.getId(), chatRoom);
//            chatRoomMaps.put(new ArrayList<>(Arrays.asList(message.getSender(), message.getSender())), message.getSender(), chatRoom);
            }
        }
    }

}
