package com.creddit.credditchatserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message extends {

    private String sender;
    private String receiver;
    private String message;
    private String createdDate;

}
