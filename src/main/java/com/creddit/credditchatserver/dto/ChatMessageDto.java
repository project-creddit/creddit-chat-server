package com.creddit.credditchatserver.dto;

import com.creddit.credditchatserver.entity.ChatMessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private Long id;

    private String chatRoomId;

    private String message;

    private String senderName;

    private ChatMessageType status;

}

