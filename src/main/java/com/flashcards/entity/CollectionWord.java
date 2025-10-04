package com.flashcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Junction table linking Collections to Words (many-to-many relationship)
 * Allows users to add globally shared words to their personal collections
 */
@Entity
@Table(name = "collection_words", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"collection_id", "word_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    @Column(name = "custom_notification_interval")
    private Integer customNotificationInterval;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
}
