package com.flashcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordDefinitionDto {
    private Long id;
    private String definition;
    private String example;
    private List<String> synonyms = new ArrayList<>();
    private List<String> antonyms = new ArrayList<>();
}