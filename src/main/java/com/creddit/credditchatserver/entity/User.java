package com.creddit.credditchatserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Deprecated
public class User {
    private String id;
    private String name;
    private String password;
}
