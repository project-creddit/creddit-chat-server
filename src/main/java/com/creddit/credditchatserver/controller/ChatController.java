package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.dto.ChatRoomIdDto;
import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.service.ChatService;
import com.creddit.credditchatserver.trace.TraceAspect;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public ResponseEntity<Stream<ChatRoom>> fetchAllChatRooms(@PathVariable String userName){
        Collection<ChatRoom> chatRooms = chatService.getChatRooms(userName);
        Stream<ChatRoom> messages = chatRooms.stream().filter(s -> s.getUsers().contains(userName));
//                .filter(s -> !s.getLeftUsers().contains(userName));

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/{userName}/chatrooms/{targetUser}/messages")
    public ResponseEntity<ChatRoom> fetchAllMessages(@PathVariable String userName, @PathVariable String targetUser){
        Collection<ChatRoom> chatRooms = chatService.getChatRooms(userName);
        ChatRoom messages = chatRooms.stream().filter(s -> s.getTarget().equals(targetUser)).findFirst().get();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/{userName}/chatroom/left")
    public ResponseEntity<ChatRoom> leftChatRoom(@RequestBody ChatRoomIdDto chatRoomId, @PathVariable String userName){
        ChatRoom chatRoom = chatService.leftChatRoom(userName, chatRoomId);

        return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }
}
