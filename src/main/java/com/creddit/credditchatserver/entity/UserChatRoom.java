package com.creddit.credditchatserver.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class UserChatRoom {

    @Id
    @GeneratedValue
    @Column(name="user_chat_room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Long userId;
}
