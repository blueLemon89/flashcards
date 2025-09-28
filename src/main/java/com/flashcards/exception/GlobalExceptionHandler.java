package com.flashcards.exception;

import com.flashcards.constant.ErrorCode;
import com.flashcards.constant.ErrorMessage;
import com.flashcards.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        log.error("Custom exception occurred: {}", ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), ex.getStatusCode());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation exception occurred: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = ErrorMessage.VALIDATION_ERROR + ": " + errors.toString();
        ApiResponse<Object> response = ApiResponse.error(message, ErrorCode.VALIDATION_ERROR);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation exception occurred: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(ErrorMessage.VALIDATION_ERROR + ": " + ex.getMessage(), ErrorCode.VALIDATION_ERROR);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception occurred: {}", ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.error(ErrorMessage.BAD_REQUEST + ": " + ex.getMessage(), ErrorCode.BAD_REQUEST);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.error("Runtime error: " + ex.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR + ": " + ex.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.ok(response);
    }
}