package com.creddit.credditchatserver.entity;

import com.creddit.credditchatserver.dto.ChatRoomDto;
import com.creddit.credditchatserver.dto.ProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    private String id;
    private String target;
    private List<ProfileResponseDto> users;
    private List<String> leftUsers;
    private List<Message> messages;
}



