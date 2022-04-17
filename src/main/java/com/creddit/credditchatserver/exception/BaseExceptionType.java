package com.creddit.credditchatserver.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {
    HttpStatus getHttpStatus();
    String getErrorMessage();
}
