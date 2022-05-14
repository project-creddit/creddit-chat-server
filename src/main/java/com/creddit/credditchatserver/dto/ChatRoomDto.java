package com.creddit.credditchatserver.dto;

import com.creddit.credditchatserver.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomDto {
    private List<ProfileResponseDto> usersInfo;
    private List<ChatRoom> chatRooms;
}
