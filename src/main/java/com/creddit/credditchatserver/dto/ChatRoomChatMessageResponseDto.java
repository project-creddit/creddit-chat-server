package com.creddit.credditchatserver.dto;

import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class ChatRoomChatMessageResponseDto {

    private Long id;
    private String uuid;
    private String name;
    private int userCount;
    private List<ChatMessageResponseDto> chatMessages;

    public ChatRoomChatMessageResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.uuid = chatRoom.getUuid();
        this.name = chatRoom.getName();
        this.userCount = chatRoom.getUserCount();
//        this.chatMessages = chatRoom.getChatMessages().stream()
//                .map(ChatMessageResponseDto::new)
//                .collect(Collectors.toList());
    }
}
