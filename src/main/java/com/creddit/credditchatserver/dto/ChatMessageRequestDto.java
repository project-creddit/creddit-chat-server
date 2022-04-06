package com.creddit.credditchatserver.dto;

import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.entity.ChatMessageType;
import com.creddit.credditchatserver.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequestDto {
    private Long id;
    private ChatRoom chatRoom;
    private String message;
    private ChatMessageType status;

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .id(id)
                .chatRoom(chatRoom)
                .status(status)
                .build();
    }
}

