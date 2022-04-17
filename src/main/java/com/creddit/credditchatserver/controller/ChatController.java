package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.service.ChatService;
import com.creddit.credditchatserver.trace.TraceAspect;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Import(TraceAspect.class)
@RequestMapping("/chat")
public class ChatController {

    private ChatService chatService;

    @GetMapping("/register/{myId}/{userId}")
    public ResponseEntity<?> register(@PathVariable String userId, @PathVariable String myId) throws Exception{

        chatService.createChatRoom(userId, myId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
