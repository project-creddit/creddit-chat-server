package com.creddit.credditchatserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String sender;
    private String receiver;
    private String chatRoomId;
    private String message;
    private String createdDate;

}
