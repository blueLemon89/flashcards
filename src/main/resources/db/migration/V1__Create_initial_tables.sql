-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    default_notification_interval INTEGER DEFAULT 60,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create collections table
CREATE TABLE collections (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create flashcards table
CREATE TABLE flashcards (
    id BIGSERIAL PRIMARY KEY,
    english_word VARCHAR(255) NOT NULL,
    vietnamese_meaning TEXT NOT NULL,
    pronunciation VARCHAR(255),
    part_of_speech VARCHAR(50),
    usage_context TEXT,
    example_sentence_english TEXT,
    example_sentence_vietnamese TEXT,
    difficulty_level VARCHAR(20) DEFAULT 'MEDIUM',
    custom_notification_interval INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    collection_id BIGINT NOT NULL,
    FOREIGN KEY (collection_id) REFERENCES collections(id) ON DELETE CASCADE
);

-- Create notification_schedules table
CREATE TABLE notification_schedules (
    id BIGSERIAL PRIMARY KEY,
    scheduled_time TIMESTAMP NOT NULL,
    notification_type VARCHAR(50) DEFAULT 'VOCABULARY_REVIEW',
    is_sent BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    flashcard_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (flashcard_id) REFERENCES flashcards(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_collections_user_id ON collections(user_id);
CREATE INDEX idx_flashcards_collection_id ON flashcards(collection_id);
CREATE INDEX idx_notification_schedules_user_id ON notification_schedules(user_id);
CREATE INDEX idx_notification_schedules_flashcard_id ON notification_schedules(flashcard_id);
CREATE INDEX idx_notification_schedules_scheduled_time ON notification_schedules(scheduled_time);
CREATE INDEX idx_notification_schedules_is_sent ON notification_schedules(is_sent);