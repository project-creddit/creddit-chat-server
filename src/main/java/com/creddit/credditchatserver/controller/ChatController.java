package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.service.ChatMessageService;
import com.creddit.credditchatserver.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @Trace
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        return chatMessage;
    }

    @MessageMapping("/private-message")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSenderName(),"/private",chatMessage);
        log.info("chat {}", chatMessage);
        return chatMessage;
    }

}
