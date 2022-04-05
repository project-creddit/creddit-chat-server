package com.creddit.credditchatserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name="chat_room_id")
    private Long id;

    private String name;

    private int userCount;
}
