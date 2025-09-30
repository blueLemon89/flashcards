package com.flashcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "word_meanings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordMeaning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @OneToMany(mappedBy = "meaning", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WordDefinition> definitions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "word_meaning_synonyms", joinColumns = @JoinColumn(name = "meaning_id"))
    @Column(name = "synonym")
    private List<String> synonyms = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "word_meaning_antonyms", joinColumns = @JoinColumn(name = "meaning_id"))
    @Column(name = "antonym")
    private List<String> antonyms = new ArrayList<>();
}