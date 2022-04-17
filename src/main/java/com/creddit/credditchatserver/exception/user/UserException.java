package com.creddit.credditchatserver.exception.user;

import com.creddit.credditchatserver.exception.BaseException;
import com.creddit.credditchatserver.exception.BaseExceptionType;

public class UserException extends BaseException {
    private final BaseExceptionType exceptionType;

    public UserException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
