package com.flashcards.service;

import com.flashcards.constant.ErrorMessage;
import com.flashcards.dto.CollectionDto;
import com.flashcards.dto.FavoriteDto;
import com.flashcards.dto.WordDto;
import com.flashcards.entity.*;
import com.flashcards.entity.Favorite.FavoriteType;
import com.flashcards.exception.FlashcardExceptions;
import com.flashcards.repository.CollectionRepository;
import com.flashcards.repository.FavoriteRepository;
import com.flashcards.repository.WordRepository;
import com.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final CollectionRepository collectionRepository;

    // Thêm word vào favorites
    public FavoriteDto addWordToFavorites(Long userId, Long wordId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_ID, userId)));

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new FlashcardExceptions.WordNotFoundException(
                    String.format(ErrorMessage.WORD_NOT_FOUND_WITH_ID, wordId)));

        // Kiểm tra đã favorite chưa
        if (favoriteRepository.existsByUserIdAndWordId(userId, wordId)) {
            throw new FlashcardExceptions.DuplicateResourceException(
                "Word đã có trong danh sách favorite");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setWord(word);
        favorite.setFavoriteType(FavoriteType.WORD);

        favorite = favoriteRepository.save(favorite);
        return convertToDto(favorite);
    }

    // Thêm collection vào favorites
    public FavoriteDto addCollectionToFavorites(Long userId, Long collectionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_ID, userId)));

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new FlashcardExceptions.CollectionNotFoundException(
                    String.format(ErrorMessage.COLLECTION_NOT_FOUND_WITH_ID, collectionId)));

        // Kiểm tra đã favorite chưa
        if (favoriteRepository.existsByUserIdAndCollectionId(userId, collectionId)) {
            throw new FlashcardExceptions.DuplicateResourceException(
                "Collection đã có trong danh sách favorite");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCollection(collection);
        favorite.setFavoriteType(FavoriteType.COLLECTION);

        favorite = favoriteRepository.save(favorite);
        return convertToDto(favorite);
    }

    // Xóa word khỏi favorites
    public void removeWordFromFavorites(Long userId, Long wordId) {
        if (!favoriteRepository.existsByUserIdAndWordId(userId, wordId)) {
            throw new FlashcardExceptions.ResourceNotFoundException(
                "Favorite không tồn tại");
        }
        favoriteRepository.deleteByUserIdAndWordId(userId, wordId);
    }

    // Xóa collection khỏi favorites
    public void removeCollectionFromFavorites(Long userId, Long collectionId) {
        if (!favoriteRepository.existsByUserIdAndCollectionId(userId, collectionId)) {
            throw new FlashcardExceptions.ResourceNotFoundException(
                "Favorite không tồn tại");
        }
        favoriteRepository.deleteByUserIdAndCollectionId(userId, collectionId);
    }

    // Lấy tất cả favorite words của user
    @Transactional(readOnly = true)
    public List<FavoriteDto> getFavoriteWords(Long userId) {
        List<Favorite> favorites = favoriteRepository.findAllFavoriteWordsByUserId(userId);
        return favorites.stream()
                .map(this::convertToDtoWithDetails)
                .collect(Collectors.toList());
    }

    // Lấy tất cả favorite collections của user
    @Transactional(readOnly = true)
    public List<FavoriteDto> getFavoriteCollections(Long userId) {
        List<Favorite> favorites = favoriteRepository.findAllFavoriteCollectionsByUserId(userId);
        return favorites.stream()
                .map(this::convertToDtoWithDetails)
                .collect(Collectors.toList());
    }

    // Lấy tất cả favorites của user
    @Transactional(readOnly = true)
    public List<FavoriteDto> getAllFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Kiểm tra word có phải favorite không
    @Transactional(readOnly = true)
    public boolean isWordFavorite(Long userId, Long wordId) {
        return favoriteRepository.existsByUserIdAndWordId(userId, wordId);
    }

    // Kiểm tra collection có phải favorite không
    @Transactional(readOnly = true)
    public boolean isCollectionFavorite(Long userId, Long collectionId) {
        return favoriteRepository.existsByUserIdAndCollectionId(userId, collectionId);
    }

    // Convert to DTO (basic)
    private FavoriteDto convertToDto(Favorite favorite) {
        FavoriteDto dto = new FavoriteDto();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUser().getId());
        dto.setFavoriteType(favorite.getFavoriteType());
        dto.setCreatedAt(favorite.getCreatedAt());

        if (favorite.getWord() != null) {
            dto.setWordId(favorite.getWord().getId());
        }
        if (favorite.getCollection() != null) {
            dto.setCollectionId(favorite.getCollection().getId());
        }

        return dto;
    }

    // Convert to DTO with details
    private FavoriteDto convertToDtoWithDetails(Favorite favorite) {
        FavoriteDto dto = convertToDto(favorite);

        if (favorite.getWord() != null) {
            dto.setWord(convertWordToDto(favorite.getWord()));
        }
        if (favorite.getCollection() != null) {
            dto.setCollection(convertCollectionToDto(favorite.getCollection()));
        }

        return dto;
    }

    // Convert Word to DTO
    private WordDto convertWordToDto(Word word) {
        WordDto dto = new WordDto();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setPhonetic(word.getPhonetic());
        dto.setAudioUrl(word.getAudioUrl());
        dto.setSourceUrl(word.getSourceUrl());
        dto.setDifficultyLevel(word.getDifficultyLevel());
        dto.setCustomNotificationInterval(word.getCustomNotificationInterval());
        dto.setIsActive(word.getIsActive());
        dto.setCreatedAt(word.getCreatedAt());
        dto.setUpdatedAt(word.getUpdatedAt());
        if (word.getCollection() != null) {
            dto.setCollectionId(word.getCollection().getId());
        }
        return dto;
    }

    // Convert Collection to DTO
    private CollectionDto convertCollectionToDto(Collection collection) {
        CollectionDto dto = new CollectionDto();
        dto.setId(collection.getId());
        dto.setName(collection.getName());
        dto.setDescription(collection.getDescription());
        dto.setIsActive(collection.getIsActive());
        dto.setCreatedAt(collection.getCreatedAt());
        dto.setUpdatedAt(collection.getUpdatedAt());
        if (collection.getUser() != null) {
            dto.setUserId(collection.getUser().getId());
        }
        return dto;
    }
}