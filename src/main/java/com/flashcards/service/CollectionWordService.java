package com.flashcards.service;

import com.flashcards.dto.CollectionWordDto;
import com.flashcards.entity.Collection;
import com.flashcards.entity.CollectionWord;
import com.flashcards.entity.Word;
import com.flashcards.exception.FlashcardExceptions.*;
import com.flashcards.mapper.CollectionWordMapper;
import com.flashcards.repository.CollectionRepository;
import com.flashcards.repository.CollectionWordRepository;
import com.flashcards.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionWordService {
    private final CollectionWordRepository collectionWordRepository;
    private final CollectionRepository collectionRepository;
    private final WordRepository wordRepository;
    private final CollectionWordMapper collectionWordMapper;

    /**
     * Add a word to a collection
     */
    public CollectionWordDto addWordToCollection(Long collectionId, Long wordId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with id: " + collectionId));

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new WordNotFoundException("Word not found with id: " + wordId));

        // Check if already exists
        if (collectionWordRepository.existsByCollectionIdAndWordId(collectionId, wordId)) {
            throw new DuplicateResourceException("Word already exists in this collection");
        }

        CollectionWord collectionWord = new CollectionWord();
        collectionWord.setCollection(collection);
        collectionWord.setWord(word);
        collectionWord = collectionWordRepository.save(collectionWord);

        return collectionWordMapper.toDto(collectionWord);
    }

    /**
     * Get all words in a collection
     */
    public List<CollectionWordDto> getWordsByCollection(Long collectionId) {
        List<CollectionWord> collectionWords = collectionWordRepository.findByCollectionIdAndIsActiveTrue(collectionId);
        return collectionWords.stream()
                .map(collectionWordMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update word settings in a collection (difficulty, notes, etc.)
     */
    public CollectionWordDto updateCollectionWord(Long collectionId, Long wordId, CollectionWordDto dto) {
        CollectionWord collectionWord = collectionWordRepository.findByCollectionIdAndWordId(collectionId, wordId)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found in collection"));

        collectionWord.setDifficultyLevel(dto.getDifficultyLevel());
        collectionWord.setCustomNotificationInterval(dto.getCustomNotificationInterval());
        collectionWord.setNotes(dto.getNotes());

        collectionWord = collectionWordRepository.save(collectionWord);
        return collectionWordMapper.toDto(collectionWord);
    }

    /**
     * Remove word from collection (soft delete)
     */
    public void removeWordFromCollection(Long collectionId, Long wordId) {
        CollectionWord collectionWord = collectionWordRepository.findByCollectionIdAndWordId(collectionId, wordId)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found in collection"));

        collectionWord.setIsActive(false);
        collectionWordRepository.save(collectionWord);
    }

    /**
     * Get all words from all collections of a user
     */
    public List<CollectionWordDto> getWordsByUser(Long userId) {
        List<CollectionWord> collectionWords = collectionWordRepository.findByUserIdAndIsActiveTrue(userId);
        return collectionWords.stream()
                .map(collectionWordMapper::toDto)
                .collect(Collectors.toList());
    }
}
