package com.flashcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flashcards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "english_word", nullable = false)
    private String englishWord;

    @Column(name = "vietnamese_meaning", nullable = false, columnDefinition = "TEXT")
    private String vietnameseMeaning;

    @Column(name = "pronunciation")
    private String pronunciation;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Column(name = "usage_context", columnDefinition = "TEXT")
    private String usageContext;

    @Column(name = "example_sentence_english", columnDefinition = "TEXT")
    private String exampleSentenceEnglish;

    @Column(name = "example_sentence_vietnamese", columnDefinition = "TEXT")
    private String exampleSentenceVietnamese;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    @Column(name = "custom_notification_interval")
    private Integer customNotificationInterval; // minutes, null = use user default

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @OneToMany(mappedBy = "flashcard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotificationSchedule> notificationSchedules;

    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
}