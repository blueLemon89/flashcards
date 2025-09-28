package com.flashcards.exception;

import com.flashcards.constant.ErrorCode;

public class FlashcardExceptions {

    public static class UserNotFoundException extends CustomException {
        public UserNotFoundException(String message) {
            super(message, ErrorCode.USER_NOT_FOUND, 404);
        }
    }

    public static class UserAlreadyExistsException extends CustomException {
        public UserAlreadyExistsException(String message) {
            super(message, ErrorCode.USER_ALREADY_EXISTS, 409);
        }
    }

    public static class CollectionNotFoundException extends CustomException {
        public CollectionNotFoundException(String message) {
            super(message, ErrorCode.COLLECTION_NOT_FOUND, 404);
        }
    }

    public static class FlashcardNotFoundException extends CustomException {
        public FlashcardNotFoundException(String message) {
            super(message, ErrorCode.FLASHCARD_NOT_FOUND, 404);
        }
    }

    public static class NotificationNotFoundException extends CustomException {
        public NotificationNotFoundException(String message) {
            super(message, ErrorCode.NOTIFICATION_NOT_FOUND, 404);
        }
    }

    public static class ValidationException extends CustomException {
        public ValidationException(String message) {
            super(message, ErrorCode.VALIDATION_ERROR, 400);
        }
    }

    public static class DatabaseException extends CustomException {
        public DatabaseException(String message) {
            super(message, ErrorCode.DATABASE_CONNECTION_ERROR, 500);
        }
    }
}