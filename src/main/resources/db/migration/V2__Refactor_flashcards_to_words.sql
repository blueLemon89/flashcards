-- Rename flashcards table to words
ALTER TABLE flashcards RENAME TO words;

-- Rename columns in words table
ALTER TABLE words RENAME COLUMN english_word TO word;
ALTER TABLE words DROP COLUMN vietnamese_meaning;
ALTER TABLE words DROP COLUMN pronunciation;
ALTER TABLE words DROP COLUMN part_of_speech;
ALTER TABLE words DROP COLUMN usage_context;
ALTER TABLE words DROP COLUMN example_sentence_english;
ALTER TABLE words DROP COLUMN example_sentence_vietnamese;

-- Add new columns to words table
ALTER TABLE words ADD COLUMN phonetic VARCHAR(255);
ALTER TABLE words ADD COLUMN audio_url VARCHAR(500);
ALTER TABLE words ADD COLUMN source_url VARCHAR(500);

-- Add unique constraint on word column
ALTER TABLE words ADD CONSTRAINT unique_word UNIQUE (word);

-- Rename flashcard_id to word_id in notification_schedules
ALTER TABLE notification_schedules RENAME COLUMN flashcard_id TO word_id;

-- Create word_phonetics table
CREATE TABLE word_phonetics (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255),
    audio_url VARCHAR(500),
    source_url VARCHAR(500),
    word_id BIGINT NOT NULL,
    FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
);

-- Create word_meanings table
CREATE TABLE word_meanings (
    id BIGSERIAL PRIMARY KEY,
    part_of_speech VARCHAR(50),
    word_id BIGINT NOT NULL,
    FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
);

-- Create word_definitions table
CREATE TABLE word_definitions (
    id BIGSERIAL PRIMARY KEY,
    definition TEXT NOT NULL,
    example TEXT,
    meaning_id BIGINT NOT NULL,
    FOREIGN KEY (meaning_id) REFERENCES word_meanings(id) ON DELETE CASCADE
);

-- Create word_meaning_synonyms table
CREATE TABLE word_meaning_synonyms (
    meaning_id BIGINT NOT NULL,
    synonym VARCHAR(255),
    FOREIGN KEY (meaning_id) REFERENCES word_meanings(id) ON DELETE CASCADE
);

-- Create word_meaning_antonyms table
CREATE TABLE word_meaning_antonyms (
    meaning_id BIGINT NOT NULL,
    antonym VARCHAR(255),
    FOREIGN KEY (meaning_id) REFERENCES word_meanings(id) ON DELETE CASCADE
);

-- Create word_definition_synonyms table
CREATE TABLE word_definition_synonyms (
    definition_id BIGINT NOT NULL,
    synonym VARCHAR(255),
    FOREIGN KEY (definition_id) REFERENCES word_definitions(id) ON DELETE CASCADE
);

-- Create word_definition_antonyms table
CREATE TABLE word_definition_antonyms (
    definition_id BIGINT NOT NULL,
    antonym VARCHAR(255),
    FOREIGN KEY (definition_id) REFERENCES word_definitions(id) ON DELETE CASCADE
);

-- Create favorites table
CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    word_id BIGINT,
    collection_id BIGINT,
    favorite_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE,
    FOREIGN KEY (collection_id) REFERENCES collections(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_word UNIQUE (user_id, word_id),
    CONSTRAINT unique_user_collection UNIQUE (user_id, collection_id),
    CONSTRAINT check_favorite_target CHECK (
        (word_id IS NOT NULL AND collection_id IS NULL) OR
        (word_id IS NULL AND collection_id IS NOT NULL)
    )
);

-- Update existing indexes
DROP INDEX IF EXISTS idx_flashcards_collection_id;
DROP INDEX IF EXISTS idx_notification_schedules_flashcard_id;

CREATE INDEX idx_words_collection_id ON words(collection_id);
CREATE INDEX idx_words_word ON words(word);
CREATE INDEX idx_notification_schedules_word_id ON notification_schedules(word_id);

-- Create new indexes for related tables
CREATE INDEX idx_word_phonetics_word_id ON word_phonetics(word_id);
CREATE INDEX idx_word_meanings_word_id ON word_meanings(word_id);
CREATE INDEX idx_word_definitions_meaning_id ON word_definitions(meaning_id);
CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_word_id ON favorites(word_id);
CREATE INDEX idx_favorites_collection_id ON favorites(collection_id);
CREATE INDEX idx_favorites_type ON favorites(favorite_type);
