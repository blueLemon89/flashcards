package com.flashcards.service;

import com.flashcards.dto.*;
import com.flashcards.entity.*;
import com.flashcards.repository.CollectionRepository;
import com.flashcards.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final CollectionRepository collectionRepository;

    @Transactional(readOnly = true)
    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findByIsActiveTrue();
        return words.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WordDto getWordById(Long id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        return convertToDto(word);
    }

    @Transactional(readOnly = true)
    public WordDto getByWord(String wordDisplay) {
        Word word = wordRepository.findByWord(wordDisplay)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        return convertToDto(word);
    }

    @Transactional
    public WordDto createWord(WordDto wordDto) {
        Word word = convertToEntity(wordDto);
        word = wordRepository.save(word);
        return convertToDto(word);
    }

    @Transactional
    public WordDto updateWord(Long id, WordDto wordDto) {
        Word existingWord = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        existingWord.setWord(wordDto.getWord());
        existingWord.setPhonetic(wordDto.getPhonetic());
        existingWord.setAudioUrl(wordDto.getAudioUrl());
        existingWord.setSourceUrl(wordDto.getSourceUrl());

        // Update phonetics
        existingWord.getPhonetics().clear();
        if (wordDto.getPhonetics() != null) {
            for (WordPhoneticDto phoneticDto : wordDto.getPhonetics()) {
                WordPhonetic phonetic = new WordPhonetic();
                phonetic.setText(phoneticDto.getText());
                phonetic.setAudioUrl(phoneticDto.getAudioUrl());
                phonetic.setSourceUrl(phoneticDto.getSourceUrl());
                phonetic.setWord(existingWord);
                existingWord.getPhonetics().add(phonetic);
            }
        }

        // Update meanings
        existingWord.getMeanings().clear();
        if (wordDto.getMeanings() != null) {
            for (WordMeaningDto meaningDto : wordDto.getMeanings()) {
                WordMeaning meaning = new WordMeaning();
                meaning.setPartOfSpeech(meaningDto.getPartOfSpeech());
                meaning.setWord(existingWord);
                meaning.setSynonyms(meaningDto.getSynonyms());
                meaning.setAntonyms(meaningDto.getAntonyms());

                // Add definitions
                if (meaningDto.getDefinitions() != null) {
                    for (WordDefinitionDto defDto : meaningDto.getDefinitions()) {
                        WordDefinition definition = new WordDefinition();
                        definition.setDefinition(defDto.getDefinition());
                        definition.setExample(defDto.getExample());
                        definition.setSynonyms(defDto.getSynonyms());
                        definition.setAntonyms(defDto.getAntonyms());
                        definition.setMeaning(meaning);
                        meaning.getDefinitions().add(definition);
                    }
                }
                existingWord.getMeanings().add(meaning);
            }
        }

        existingWord = wordRepository.save(existingWord);
        return convertToDto(existingWord);
    }

    @Transactional
    public void deleteWord(Long id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        word.setIsActive(false);
        wordRepository.save(word);
    }

    @Transactional(readOnly = true)
    public List<WordDto> searchWords(String keyword) {
        List<Word> words = wordRepository.findByWordContainingIgnoreCaseAndIsActiveTrue(keyword);
        return words.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private WordDto convertToDto(Word word) {
        WordDto dto = new WordDto();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setPhonetic(word.getPhonetic());
        dto.setAudioUrl(word.getAudioUrl());
        dto.setSourceUrl(word.getSourceUrl());
        dto.setIsActive(word.getIsActive());
        dto.setCreatedAt(word.getCreatedAt());
        dto.setUpdatedAt(word.getUpdatedAt());

        // Convert phonetics
        if (word.getPhonetics() != null) {
            dto.setPhonetics(word.getPhonetics().stream()
                    .map(this::convertPhoneticToDto)
                    .collect(Collectors.toList()));
        }

        // Convert meanings
        if (word.getMeanings() != null) {
            dto.setMeanings(word.getMeanings().stream()
                    .map(this::convertMeaningToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private WordPhoneticDto convertPhoneticToDto(WordPhonetic phonetic) {
        WordPhoneticDto dto = new WordPhoneticDto();
        dto.setId(phonetic.getId());
        dto.setText(phonetic.getText());
        dto.setAudioUrl(phonetic.getAudioUrl());
        dto.setSourceUrl(phonetic.getSourceUrl());
        return dto;
    }

    private WordMeaningDto convertMeaningToDto(WordMeaning meaning) {
        WordMeaningDto dto = new WordMeaningDto();
        dto.setId(meaning.getId());
        dto.setPartOfSpeech(meaning.getPartOfSpeech());

        // Force initialization of lazy collections
        if (meaning.getSynonyms() != null) {
            dto.setSynonyms(new java.util.ArrayList<>(meaning.getSynonyms()));
        }
        if (meaning.getAntonyms() != null) {
            dto.setAntonyms(new java.util.ArrayList<>(meaning.getAntonyms()));
        }

        if (meaning.getDefinitions() != null) {
            dto.setDefinitions(meaning.getDefinitions().stream()
                    .map(this::convertDefinitionToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private WordDefinitionDto convertDefinitionToDto(WordDefinition definition) {
        WordDefinitionDto dto = new WordDefinitionDto();
        dto.setId(definition.getId());
        dto.setDefinition(definition.getDefinition());
        dto.setExample(definition.getExample());

        // Force initialization of lazy collections
        if (definition.getSynonyms() != null) {
            dto.setSynonyms(new java.util.ArrayList<>(definition.getSynonyms()));
        }
        if (definition.getAntonyms() != null) {
            dto.setAntonyms(new java.util.ArrayList<>(definition.getAntonyms()));
        }

        return dto;
    }

    private Word convertToEntity(WordDto dto) {
        Word word = new Word();
        word.setWord(dto.getWord());
        word.setPhonetic(dto.getPhonetic());
        word.setAudioUrl(dto.getAudioUrl());
        word.setSourceUrl(dto.getSourceUrl());
        return word;
    }
}