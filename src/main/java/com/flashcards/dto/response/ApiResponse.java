package com.flashcards.dto.response;

import com.flashcards.constant.ErrorCode;
import com.flashcards.constant.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String statusCode;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, ErrorMessage.SUCCESS, ErrorCode.SUCCESS, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, ErrorCode.SUCCESS, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message, String statusCode) {
        return new ApiResponse<>(false, message, statusCode, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, ErrorCode.INTERNAL_SERVER_ERROR, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, message, ErrorCode.BAD_REQUEST, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, message, ErrorCode.NOT_FOUND, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(false, message, ErrorCode.CONFLICT, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> validationError(String message) {
        return new ApiResponse<>(false, message, ErrorCode.VALIDATION_ERROR, null, LocalDateTime.now());
    }
}