package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.service.ChatService;
import com.creddit.credditchatserver.trace.TraceAspect;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@RestController
@Import(TraceAspect.class)
@RequestMapping("/chat")
public class ChatController {

    private ChatService chatService;

    @GetMapping("/register/{myId}/{userId}")
    public ResponseEntity<?> registerChatRoom(@PathVariable String userId, @PathVariable String myId) throws Exception{
        chatService.createChatRoom(userId, myId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userName}/chatrooms")
    public ResponseEntity<Collection<ChatRoom>> fetchAllChatRooms(@PathVariable String userName){
        Collection<ChatRoom> chatRooms = chatService.getChatRooms(userName);
        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }

    @GetMapping("/{userName}/chatrooms/{targetUser}/messages")
    public ResponseEntity<Stream<ChatRoom>> fetchAllMessages(@PathVariable String userName, @PathVariable String targetUser){
        Collection<ChatRoom> chatRooms = chatService.getChatRooms(userName);
        Stream<ChatRoom> messages = chatRooms.stream().filter(s -> s.getTarget().equals(targetUser));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
