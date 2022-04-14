package com.creddit.credditchatserver.dto;

import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.entity.ChatMessageType;
import com.creddit.credditchatserver.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
@Getter
public class ChatRoomResponseDto {
    private Long id;
    private String uuid;
    private String name;
    private int userCount;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.uuid = chatRoom.getUuid();
        this.name = chatRoom.getName();
        this.userCount = chatRoom.getUserCount();
    }
}
