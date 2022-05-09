package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.dto.ChatRoomIdDto;
import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.exception.user.UserException;
import com.creddit.credditchatserver.exception.user.UserExceptionType;
//import com.creddit.credditchatserver.repository.ChatRoomRepository;
import com.creddit.credditchatserver.trace.Trace;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ChatService {

//    private ChatRoomRepository chatRoomRepository;
    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    private RedisTemplate<String, User> userTemplate;

    @Trace
    public ChatRoom leftChatRoom(String userName, ChatRoomIdDto chatRoomId) {
        HashOperations<String, String, ChatRoom> hashOperations = chatRoomRedisTemplate.opsForHash();
        Map<String, ChatRoom> mapper = hashOperations.entries(userName);
        ChatRoom chatRoom = mapper.values().stream().filter(s -> s.getId().equals(chatRoomId.getChatRoomId())).findFirst().get();
        List<String> leftUsers = chatRoom.getLeftUsers();
        leftUsers.add(userName);
        chatRoom.setLeftUsers(leftUsers);
        hashOperations.put(userName, chatRoom.getTarget() ,chatRoom);
        return chatRoom;
    }

    @Trace
    public void createChatMessage(Message message) {
        HashOperations<String, String, ChatRoom> chatRoomMaps = chatRoomRedisTemplate.opsForHash();
        String sender = message.getSender();
        String receiver = message.getReceiver();
        boolean hasSenderMessages = hasMessages(chatRoomMaps, sender, receiver);
        boolean hasReceiverMessages = hasMessages(chatRoomMaps, receiver, sender);
        getChatRoomAndUpdateMessage(message, chatRoomMaps, sender, receiver, hasSenderMessages, "SENDER");
        getChatRoomAndUpdateMessage(message, chatRoomMaps, receiver, sender, hasReceiverMessages, "RECEIVER");

    }
    private void getChatRoomAndUpdateMessage(
            Message message,
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            String myId,
            String userId,
            boolean hasMessages,
            String messageType
    ) {
        ChatRoom chatRoom = hasMessages == true ? chatRoomMaps.get(myId, userId) : new ChatRoom(
                UUID.randomUUID().toString(),
                userId,
                new ArrayList<>(Arrays.asList(myId, userId)),
                new ArrayList<>(),
                new ArrayList<>()
        );
        updateChatRoomAndMessages(chatRoomMaps, chatRoom, message, messageType);
    }
    private boolean hasMessages(
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            String myId,
            String userId) {
        return chatRoomMaps.hasKey(myId, userId);
    }

    public void createChatRoom(String userId, String myId) throws Exception{
//        if(!userTemplate.hasKey(userId + ":info")){
//            throw new UserException(UserExceptionType.NOT_FOUNT_USER);
//        }

        HashOperations<String, String, ChatRoom> chatRoomMaps = chatRoomRedisTemplate.opsForHash();

        ChatRoom chatRoom = chatRoomMaps.get(myId, userId);
        if (chatRoom.getLeftUsers().contains(myId)) {
            throw new UserException(UserExceptionType.ALREADY_EXIST_CHAT_USER);

        }
//        if(chatRoomMaps.hasKey(myId, userId)){
//            throw new UserException(UserExceptionType.ALREADY_EXIST_CHAT_USER);
//        }
        chatRoomMaps.put(myId, userId, new ChatRoom(
                UUID.randomUUID().toString(),
                userId,
                new ArrayList<>(Arrays.asList(myId, userId)),
                new ArrayList<>(),
                new ArrayList<>()));
    }

    public Collection<ChatRoom> getChatRooms(String userName){
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
        if (Objects.equals(messageType, "SENDER")) {
            chatRoomMaps.put(message.getSender(), message.getReceiver(), chatRoom);
//            chatRoomMaps.put()
        } else {
            chatRoomMaps.put(message.getReceiver(), message.getSender(), chatRoom);
//            chatRoomMaps.put(new ArrayList<>(Arrays.asList(message.getSender(), message.getSender())), message.getSender(), chatRoom);
        }
    }

}
