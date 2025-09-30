package com.flashcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordPhoneticDto {
    private Long id;
    private String text;
    private String audioUrl;
    private String sourceUrl;
}