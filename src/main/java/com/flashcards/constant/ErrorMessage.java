package com.flashcards.constant;

public final class ErrorMessage {

    // Generic messages
    public static final String SUCCESS = "Operation completed successfully";
    public static final String BAD_REQUEST = "Invalid request parameters";
    public static final String UNAUTHORIZED = "Authentication required";
    public static final String FORBIDDEN = "Access denied";
    public static final String NOT_FOUND = "Resource not found";
    public static final String CONFLICT = "Resource already exists";
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error occurred";

    // User related messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id: %s";
    public static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: %s";
    public static final String USER_ALREADY_EXISTS_USERNAME = "Username already exists: %s";
    public static final String USER_ALREADY_EXISTS_EMAIL = "Email already exists: %s";
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully";
    public static final String USER_UPDATED_SUCCESS = "User updated successfully";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";

    // Collection related messages
    public static final String COLLECTION_NOT_FOUND = "Collection not found";
    public static final String COLLECTION_NOT_FOUND_WITH_ID = "Collection not found with id: %s";
    public static final String COLLECTION_ALREADY_EXISTS = "Collection already exists with name: %s";
    public static final String COLLECTION_CREATED_SUCCESS = "Collection created successfully";
    public static final String COLLECTION_UPDATED_SUCCESS = "Collection updated successfully";
    public static final String COLLECTION_DELETED_SUCCESS = "Collection deleted successfully";
    public static final String COLLECTION_ACCESS_DENIED = "You don't have permission to access this collection";

    // Flashcard related messages
    public static final String FLASHCARD_NOT_FOUND = "Flashcard not found";
    public static final String FLASHCARD_NOT_FOUND_WITH_ID = "Flashcard not found with id: %s";
    public static final String FLASHCARD_ALREADY_EXISTS = "Flashcard already exists with word: %s";
    public static final String FLASHCARD_CREATED_SUCCESS = "Flashcard created successfully";
    public static final String FLASHCARD_UPDATED_SUCCESS = "Flashcard updated successfully";
    public static final String FLASHCARD_DELETED_SUCCESS = "Flashcard deleted successfully";
    public static final String FLASHCARD_ACCESS_DENIED = "You don't have permission to access this flashcard";

    // Notification related messages
    public static final String NOTIFICATION_NOT_FOUND = "Notification not found";
    public static final String NOTIFICATION_NOT_FOUND_WITH_ID = "Notification not found with id: %s";
    public static final String NOTIFICATION_ALREADY_SENT = "Notification already sent";
    public static final String NOTIFICATION_SCHEDULED_SUCCESS = "Notification scheduled successfully";
    public static final String NOTIFICATION_CANCELLED_SUCCESS = "Notification cancelled successfully";
    public static final String NOTIFICATION_SCHEDULE_FAILED = "Failed to schedule notification";

    // Database related messages
    public static final String DATABASE_CONNECTION_ERROR = "Database connection failed";
    public static final String DATABASE_CONSTRAINT_VIOLATION = "Database constraint violation";
    public static final String DATABASE_TIMEOUT = "Database operation timeout";

    // Validation messages
    public static final String VALIDATION_USERNAME_REQUIRED = "Username is required";
    public static final String VALIDATION_USERNAME_SIZE = "Username must be between 3 and 50 characters";
    public static final String VALIDATION_EMAIL_REQUIRED = "Email is required";
    public static final String VALIDATION_EMAIL_FORMAT = "Email should be valid";
    public static final String VALIDATION_PASSWORD_REQUIRED = "Password is required";
    public static final String VALIDATION_PASSWORD_SIZE = "Password must be at least 6 characters";
    public static final String VALIDATION_COLLECTION_NAME_REQUIRED = "Collection name is required";
    public static final String VALIDATION_ENGLISH_WORD_REQUIRED = "English word is required";
    public static final String VALIDATION_VIETNAMESE_MEANING_REQUIRED = "Vietnamese meaning is required";

    private ErrorMessage() {
        // Private constructor to prevent instantiation
    }
}