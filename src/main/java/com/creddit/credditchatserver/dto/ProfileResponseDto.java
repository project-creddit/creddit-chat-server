package com.creddit.credditchatserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ProfileResponseDto {

    private String nickname;
    private String introduction;
    private ImageDto image;
    @JsonIgnore
    private LocalDateTime createdDate;
}
