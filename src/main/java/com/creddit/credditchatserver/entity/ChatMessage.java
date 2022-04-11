package com.creddit.credditchatserver.entity;

import com.creddit.credditchatserver.dto.ChatMessageDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "chat_messages")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class ChatMessage {

    @Id
    @GeneratedValue
    @Column(name="chat_message_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    private String message;

    private String senderName;

    private ChatMessageType status;

    public static ChatMessage createChatMessage(ChatMessageDto chatMessageDto, ChatRoom chatRoom) {
        ChatMessage chatMessage = new ChatMessage();
        log.info("chatMessageDto: {}", chatMessageDto);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setMessage(chatMessageDto.getMessage());
        chatMessage.setSenderName(chatMessage.getSenderName());
        chatMessage.setStatus(chatMessage.getStatus());
        return chatMessage;
    }

}


