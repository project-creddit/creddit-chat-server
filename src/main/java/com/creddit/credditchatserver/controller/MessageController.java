package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.service.ChatService;
import com.creddit.credditchatserver.trace.Trace;
import com.creddit.credditchatserver.trace.TraceAspect;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
@RestController
@Import(TraceAspect.class)
public class ChatController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    private ChatService chatService;


    @MessageMapping("/send")
    public void SendToMessage(Message msg) {
        chatService.createChatMessage(msg);
        simpMessagingTemplate.convertAndSend("/topic/" + msg.getReceiver(), msg);
    }

}
