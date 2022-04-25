package com.creddit.credditchatserver.exception.user;

import com.creddit.credditchatserver.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {
    ALREADY_EXIST_CHAT_USER(HttpStatus.BAD_REQUEST, "이미 채팅유저로 등록이 된 회원입니다"),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 가입된 유저입니다."),
    NOT_FOUNT_USER(HttpStatus.NOT_FOUND, "일치하는 회원 정보가 없습니다.");

    private HttpStatus status;
    private String errorMessage;

    UserExceptionType(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

}
