package com.flashcards.constant;

public final class ErrorCode {

    // Success codes
    public static final String SUCCESS = "FC200";

    // Client error codes (4xx)
    public static final String BAD_REQUEST = "FC400";
    public static final String UNAUTHORIZED = "FC401";
    public static final String FORBIDDEN = "FC403";
    public static final String NOT_FOUND = "FC404";
    public static final String METHOD_NOT_ALLOWED = "FC405";
    public static final String CONFLICT = "FC409";
    public static final String VALIDATION_ERROR = "FC422";

    // Server error codes (5xx)
    public static final String INTERNAL_SERVER_ERROR = "FC500";
    public static final String NOT_IMPLEMENTED = "FC501";
    public static final String BAD_GATEWAY = "FC502";
    public static final String SERVICE_UNAVAILABLE = "FC503";
    public static final String GATEWAY_TIMEOUT = "FC504";

    // Business logic specific error codes
    public static final String USER_NOT_FOUND = "FC1001";
    public static final String USER_ALREADY_EXISTS = "FC1002";
    public static final String INVALID_CREDENTIALS = "FC1003";

    public static final String COLLECTION_NOT_FOUND = "FC2001";
    public static final String COLLECTION_ALREADY_EXISTS = "FC2002";
    public static final String COLLECTION_ACCESS_DENIED = "FC2003";

    public static final String WORD_NOT_FOUND = "FC3001";
    public static final String WORD_ALREADY_EXISTS = "FC3002";
    public static final String WORD_ACCESS_DENIED = "FC3003";

    // Legacy - để backward compatibility
    public static final String FLASHCARD_NOT_FOUND = "FC3001";
    public static final String FLASHCARD_ALREADY_EXISTS = "FC3002";
    public static final String FLASHCARD_ACCESS_DENIED = "FC3003";

    public static final String NOTIFICATION_NOT_FOUND = "FC4001";
    public static final String NOTIFICATION_ALREADY_SENT = "FC4002";
    public static final String NOTIFICATION_SCHEDULE_FAILED = "FC4003";

    // Favorite related error codes
    public static final String DUPLICATE_RESOURCE = "FC4101";
    public static final String RESOURCE_NOT_FOUND = "FC4102";

    // Database related error codes
    public static final String DATABASE_CONNECTION_ERROR = "FC5001";
    public static final String DATABASE_CONSTRAINT_VIOLATION = "FC5002";
    public static final String DATABASE_TIMEOUT = "FC5003";

    private ErrorCode() {
        // Private constructor to prevent instantiation
    }
}