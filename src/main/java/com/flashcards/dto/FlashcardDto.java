package com.flashcards.dto;

import com.flashcards.entity.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardDto {
    private Long id;

    @NotBlank(message = "English word is required")
    private String englishWord;

    @NotBlank(message = "Vietnamese meaning is required")
    private String vietnameseMeaning;

    private String pronunciation;
    private String partOfSpeech;
    private String usageContext;
    private String exampleSentenceEnglish;
    private String exampleSentenceVietnamese;
    private Flashcard.DifficultyLevel difficultyLevel;
    private Integer customNotificationInterval;

    @NotNull(message = "Collection ID is required")
    private Long collectionId;

    private String collectionName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}