package com.creddit.credditchatserver.dto;

import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.entity.ChatMessageType;
import com.creddit.credditchatserver.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatMessageResponseDto {
    private Long id;
    private ChatRoom chatRoom;
    private String message;
    private ChatMessageType status;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.chatRoom = chatMessage.getChatRoom();
        this.message = chatMessage.getMessage();
        this.status = chatMessage.getStatus();
    }
}
