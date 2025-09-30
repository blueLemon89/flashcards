package com.flashcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "word_definitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "definition", columnDefinition = "TEXT", nullable = false)
    private String definition;

    @Column(name = "example", columnDefinition = "TEXT")
    private String example;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meaning_id", nullable = false)
    private WordMeaning meaning;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "word_definition_synonyms", joinColumns = @JoinColumn(name = "definition_id"))
    @Column(name = "synonym")
    private List<String> synonyms = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "word_definition_antonyms", joinColumns = @JoinColumn(name = "definition_id"))
    @Column(name = "antonym")
    private List<String> antonyms = new ArrayList<>();
}