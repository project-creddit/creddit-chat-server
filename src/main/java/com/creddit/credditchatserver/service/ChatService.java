package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.trace.Trace;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ChatService {

    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;

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
        if (hasReceiverMessages(chatRoomMaps, message)) { // 상대방에게 대화 데이터가 있을때
            ChatRoom chatRoom = chatRoomMaps.get(message.getReceiver(), message.getSender());
            updateChatRoomAndMessages(chatRoomMaps, chatRoom, message);
        } else { // 상대방에게 대화 데이터가 없을때
            ChatRoom chatRoom = new ChatRoom(message.getSender(), new ArrayList<>());
            updateChatRoomAndMessages(chatRoomMaps, chatRoom, message);
        }
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
