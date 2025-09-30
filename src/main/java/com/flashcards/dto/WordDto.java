package com.flashcards.dto;

import com.flashcards.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {
    private Long id;

    @NotBlank(message = "Word is required")
    private String word;

    private String phonetic;
    private String audioUrl;
    private String sourceUrl;

    private Word.DifficultyLevel difficultyLevel;
    private Integer customNotificationInterval;

    @NotNull(message = "Collection ID is required")
    private Long collectionId;

    private String collectionName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Related data
    private List<WordPhoneticDto> phonetics = new ArrayList<>();
    private List<WordMeaningDto> meanings = new ArrayList<>();
}