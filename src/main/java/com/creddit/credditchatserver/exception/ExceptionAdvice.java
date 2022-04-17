package com.creddit.credditchatserver.exception;

import com.creddit.credditchatserver.trace.TraceAspect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Import(TraceAspect.class)
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseException(BaseException exception) {
        log.error("BaseException errorMessage(): {}", exception.getExceptionType().getErrorMessage());
        log.error("BaseException errorMessage(): {}", exception.getExceptionType().getHttpStatus());
        return new ResponseEntity(new ExceptionDto(
                exception.getExceptionType().getHttpStatus().value(),
                exception.getExceptionType().getHttpStatus(),
                exception.getExceptionType().getErrorMessage()
        ), HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    static class ExceptionDto {
        private int status;
        private HttpStatus statusMessage;
        private String errorMessage;
    }
}
