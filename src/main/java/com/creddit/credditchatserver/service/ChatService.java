package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.exception.user.UserException;
import com.creddit.credditchatserver.exception.user.UserExceptionType;
import com.creddit.credditchatserver.trace.Trace;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@AllArgsConstructor
public class ChatService {

    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    private RedisTemplate<String, User> userTemplate;

    @Trace
    public void createChatMessage(Message message) {
        HashOperations<String, String, ChatRoom> chatRoomMaps = chatRoomRedisTemplate.opsForHash();
        if (hasSenderMessages(chatRoomMaps, message)) {
            ChatRoom chatRoom = chatRoomMaps.get(message.getSender(), message.getReceiver());
            updateChatRoomAndMessages(chatRoomMaps, chatRoom, message);
        } else {
            ChatRoom chatRoom = new ChatRoom(message.getReceiver(), new ArrayList<>());
            updateChatRoomAndMessages(chatRoomMaps, chatRoom, message);
        }
        if (hasReceiverMessages(chatRoomMaps, message)) {
            ChatRoom chatRoom = chatRoomMaps.get(message.getReceiver(), message.getSender());
            updateChatRoomAndMessages(chatRoomMaps, chatRoom, message);
        } else {
            ChatRoom chatRoom = new ChatRoom(message.getSender(), new ArrayList<>());
            updateChatRoomAndMessages(chatRoomMaps, chatRoom, message);
        }
    }

    public void createChatRoom(String userId, String myId) throws Exception{
        if(Boolean.FALSE.equals(userTemplate.hasKey(userId + ":info"))){
            throw new UserException(UserExceptionType.NOT_FOUNT_USER);
        }
        HashOperations<String, String, ChatRoom> chatRoomMaps = chatRoomRedisTemplate.opsForHash();
        if(chatRoomMaps.hasKey(myId, userId)){
            throw new UserException(UserExceptionType.ALREADY_EXIST_CHAT_USER);
        }
        chatRoomMaps.put(myId, userId, new ChatRoom(userId, new ArrayList<>()));
    }

    public Collection<ChatRoom> getChatRooms(String userName){
        HashOperations<String, String, ChatRoom> hashOperations = chatRoomRedisTemplate.opsForHash();
        Map<String, ChatRoom> mapper = hashOperations.entries(userName);
        return mapper.values();
    }

    private boolean hasSenderMessages(
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            Message message) {
        return chatRoomMaps.hasKey(message.getSender(), message.getReceiver());
    }

    private boolean hasReceiverMessages(
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            Message message) {
        return chatRoomMaps.hasKey(message.getReceiver(), message.getSender());
    }

    private void updateChatRoomAndMessages(
            HashOperations<String, String, ChatRoom> chatRoomMaps,
            ChatRoom chatRoom,
            Message message) {
        chatRoom.getMessages().add(message);
        chatRoomMaps.put(message.getReceiver(), message.getSender(), chatRoom);
    }

}
