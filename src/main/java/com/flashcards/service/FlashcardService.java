package com.flashcards.service;

import com.flashcards.dto.FlashcardDto;
import com.flashcards.entity.Collection;
import com.flashcards.entity.Flashcard;
import com.flashcards.repository.CollectionRepository;
import com.flashcards.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final CollectionRepository collectionRepository;

    public List<FlashcardDto> getAllFlashcardsByCollection(Long collectionId) {
        List<Flashcard> flashcards = flashcardRepository.findByCollectionIdAndIsActiveTrue(collectionId);
        return flashcards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<FlashcardDto> getAllFlashcardsByUser(Long userId) {
        List<Flashcard> flashcards = flashcardRepository.findByUserIdAndIsActiveTrue(userId);
        return flashcards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public FlashcardDto getFlashcardById(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        return convertToDto(flashcard);
    }

    public FlashcardDto createFlashcard(FlashcardDto flashcardDto) {
        Collection collection = collectionRepository.findById(flashcardDto.getCollectionId())
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Flashcard flashcard = convertToEntity(flashcardDto);
        flashcard.setCollection(collection);
        flashcard = flashcardRepository.save(flashcard);
        return convertToDto(flashcard);
    }

    public FlashcardDto updateFlashcard(Long id, FlashcardDto flashcardDto) {
        Flashcard existingFlashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        existingFlashcard.setEnglishWord(flashcardDto.getEnglishWord());
        existingFlashcard.setVietnameseMeaning(flashcardDto.getVietnameseMeaning());
        existingFlashcard.setPronunciation(flashcardDto.getPronunciation());
        existingFlashcard.setPartOfSpeech(flashcardDto.getPartOfSpeech());
        existingFlashcard.setUsageContext(flashcardDto.getUsageContext());
        existingFlashcard.setExampleSentenceEnglish(flashcardDto.getExampleSentenceEnglish());
        existingFlashcard.setExampleSentenceVietnamese(flashcardDto.getExampleSentenceVietnamese());
        existingFlashcard.setDifficultyLevel(flashcardDto.getDifficultyLevel());
        existingFlashcard.setCustomNotificationInterval(flashcardDto.getCustomNotificationInterval());

        existingFlashcard = flashcardRepository.save(existingFlashcard);
        return convertToDto(existingFlashcard);
    }

    public void deleteFlashcard(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        flashcard.setIsActive(false);
        flashcardRepository.save(flashcard);
    }

    public List<FlashcardDto> searchFlashcards(String keyword) {
        List<Flashcard> flashcards = flashcardRepository.findByEnglishWordContainingIgnoreCaseAndIsActiveTrue(keyword);
        return flashcards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private FlashcardDto convertToDto(Flashcard flashcard) {
        FlashcardDto dto = new FlashcardDto();
        dto.setId(flashcard.getId());
        dto.setEnglishWord(flashcard.getEnglishWord());
        dto.setVietnameseMeaning(flashcard.getVietnameseMeaning());
        dto.setPronunciation(flashcard.getPronunciation());
        dto.setPartOfSpeech(flashcard.getPartOfSpeech());
        dto.setUsageContext(flashcard.getUsageContext());
        dto.setExampleSentenceEnglish(flashcard.getExampleSentenceEnglish());
        dto.setExampleSentenceVietnamese(flashcard.getExampleSentenceVietnamese());
        dto.setDifficultyLevel(flashcard.getDifficultyLevel());
        dto.setCustomNotificationInterval(flashcard.getCustomNotificationInterval());
        dto.setCollectionId(flashcard.getCollection().getId());
        dto.setCollectionName(flashcard.getCollection().getName());
        dto.setIsActive(flashcard.getIsActive());
        dto.setCreatedAt(flashcard.getCreatedAt());
        dto.setUpdatedAt(flashcard.getUpdatedAt());
        return dto;
    }

    private Flashcard convertToEntity(FlashcardDto dto) {
        Flashcard flashcard = new Flashcard();
        flashcard.setEnglishWord(dto.getEnglishWord());
        flashcard.setVietnameseMeaning(dto.getVietnameseMeaning());
        flashcard.setPronunciation(dto.getPronunciation());
        flashcard.setPartOfSpeech(dto.getPartOfSpeech());
        flashcard.setUsageContext(dto.getUsageContext());
        flashcard.setExampleSentenceEnglish(dto.getExampleSentenceEnglish());
        flashcard.setExampleSentenceVietnamese(dto.getExampleSentenceVietnamese());
        flashcard.setDifficultyLevel(dto.getDifficultyLevel());
        flashcard.setCustomNotificationInterval(dto.getCustomNotificationInterval());
        return flashcard;
    }
}