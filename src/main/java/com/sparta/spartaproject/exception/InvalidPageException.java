package com.sparta.spartaproject.exception;

public class InvalidPageException extends BusinessException {

    public InvalidPageException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidPageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
