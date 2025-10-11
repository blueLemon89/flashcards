package com.flashcards.service;

import com.flashcards.dto.*;
import com.flashcards.entity.*;
import com.flashcards.mapper.WordMapper;
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
    private final WordMapper wordMapper;

    @Transactional(readOnly = true)
    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findByIsActiveTrue();
        return words.stream().map(wordMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WordDto getWordById(Long id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        return wordMapper.toDto(word);
    }

    @Transactional(readOnly = true)
    public WordDto getByWord(String wordDisplay) {
        Word word = wordRepository.findByWord(wordDisplay)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        return wordMapper.toDto(word);
    }

    @Transactional
    public WordDto createWord(WordDto wordDto) {
        Word word = wordMapper.toEntity(wordDto);
        word = wordRepository.save(word);
        return wordMapper.toDto(word);
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
                WordPhonetic phonetic = wordMapper.toEntity(phoneticDto);
                phonetic.setWord(existingWord);
                existingWord.getPhonetics().add(phonetic);
            }
        }

        // Update meanings
        existingWord.getMeanings().clear();
        if (wordDto.getMeanings() != null) {
            for (WordMeaningDto meaningDto : wordDto.getMeanings()) {
                WordMeaning meaning = wordMapper.toEntity(meaningDto);
                meaning.setWord(existingWord);

                // Add definitions
                if (meaningDto.getDefinitions() != null) {
                    for (WordDefinitionDto defDto : meaningDto.getDefinitions()) {
                        WordDefinition definition = wordMapper.toEntity(defDto);
                        definition.setMeaning(meaning);
                        meaning.getDefinitions().add(definition);
                    }
                }
                existingWord.getMeanings().add(meaning);
            }
        }

        existingWord = wordRepository.save(existingWord);
        return wordMapper.toDto(existingWord);
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
        return words.stream().map(wordMapper::toDto).collect(Collectors.toList());
    }
}