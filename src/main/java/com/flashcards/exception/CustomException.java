package com.flashcards.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String statusCode;
    private final int httpStatus;

    public CustomException(String message, String statusCode, int httpStatus) {
        super(message);
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.httpStatus = 500;
    }

    public CustomException(String message) {
        super(message);
        this.statusCode = "FC500";
        this.httpStatus = 500;
    }
}