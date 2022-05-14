package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.entity.Message;
import com.creddit.credditchatserver.service.ChatService;
import com.creddit.credditchatserver.trace.TraceAspect;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@Import(TraceAspect.class)
public class MessageController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatService chatService;


    @MessageMapping("/send")
    public void SendToMessage(Message msg) throws JsonProcessingException {
        chatService.createChatMessage(msg);
        simpMessagingTemplate.convertAndSend("/topic/" + msg.getChatRoomId(), msg);
    }

}
