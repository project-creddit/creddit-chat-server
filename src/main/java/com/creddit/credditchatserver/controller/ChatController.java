package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.dto.ChatMessageDto;
import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.service.ChatMessageService;
import com.creddit.credditchatserver.service.ChatRoomService;
import com.creddit.credditchatserver.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @Trace
    @MessageMapping("/message")
    public ChatMessageDto register(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {
        chatMessageService.createChatMessage(chatMessageDto);
        return chatMessageDto;
    }

    @MessageMapping("/private-message")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSenderName(),"/private",chatMessage);
        return chatMessage;
    }

}
