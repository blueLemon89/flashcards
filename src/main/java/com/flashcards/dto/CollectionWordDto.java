package com.flashcards.dto;

import com.flashcards.entity.CollectionWord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionWordDto {
    private Long id;

    @NotNull(message = "Collection ID is required")
    private Long collectionId;

    @NotNull(message = "Word ID is required")
    private Long wordId;

    private String word;
    private String phonetic;
    private String audioUrl;

    private CollectionWord.DifficultyLevel difficultyLevel;
    private Integer customNotificationInterval;
    private String notes;

    private Boolean isActive;
    private LocalDateTime createdAt;
}
