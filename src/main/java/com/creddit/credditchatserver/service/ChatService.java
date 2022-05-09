package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.exception.user.UserException;
import com.creddit.credditchatserver.exception.user.UserExceptionType;
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

    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    private RedisTemplate<String, User> userTemplate;

//    @Trace
//    public void leftChatRoom()

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
        if(chatRoomMaps.hasKey(myId, userId)){
            throw new UserException(UserExceptionType.ALREADY_EXIST_CHAT_USER);
        }
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
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            ChatRoom chatRoom,
            Message message,
            String messageType) {
        chatRoom.getMessages().add(message);
        if (Objects.equals(messageType, "SENDER")) {
            chatRoomMaps.put(message.getSender(), message.getReceiver(), chatRoom);
        } else {
            chatRoomMaps.put(message.getReceiver(), message.getSender(), chatRoom);
        }
    }

}
