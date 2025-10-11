-- Create collection_words junction table (PostgreSQL)
-- This table links collections to words (many-to-many relationship)
-- Allows users to add globally shared words to their personal collections

CREATE TABLE IF NOT EXISTS collection_words (
    id BIGSERIAL PRIMARY KEY,
    collection_id BIGINT NOT NULL,
    word_id BIGINT NOT NULL,
    difficulty_level VARCHAR(20) DEFAULT 'MEDIUM',
    custom_notification_interval INTEGER,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign keys
    CONSTRAINT fk_collection_words_collection
        FOREIGN KEY (collection_id) REFERENCES collections(id) ON DELETE CASCADE,
    CONSTRAINT fk_collection_words_word
        FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE,

    -- Unique constraint to prevent duplicate word in same collection
    CONSTRAINT uk_collection_word UNIQUE (collection_id, word_id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_collection_words_collection_id ON collection_words(collection_id);
CREATE INDEX IF NOT EXISTS idx_collection_words_word_id ON collection_words(word_id);
CREATE INDEX IF NOT EXISTS idx_collection_words_is_active ON collection_words(is_active);
CREATE INDEX IF NOT EXISTS idx_collection_words_collection_active ON collection_words(collection_id, is_active);

-- Add comments (PostgreSQL style)
COMMENT ON TABLE collection_words IS 'Junction table linking collections to words with user-specific settings';
COMMENT ON COLUMN collection_words.id IS 'Primary key';
COMMENT ON COLUMN collection_words.collection_id IS 'Reference to collection';
COMMENT ON COLUMN collection_words.word_id IS 'Reference to word';
COMMENT ON COLUMN collection_words.difficulty_level IS 'User-specific difficulty level: EASY, MEDIUM, HARD';
COMMENT ON COLUMN collection_words.custom_notification_interval IS 'Custom notification interval in minutes for this word in this collection';
COMMENT ON COLUMN collection_words.notes IS 'User notes about this word';
COMMENT ON COLUMN collection_words.is_active IS 'Soft delete flag';
COMMENT ON COLUMN collection_words.created_at IS 'Timestamp when word was added to collection';
