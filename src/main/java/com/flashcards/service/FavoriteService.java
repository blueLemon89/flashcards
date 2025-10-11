package com.flashcards.service;

import com.flashcards.constant.ErrorMessage;
import com.flashcards.dto.CollectionDto;
import com.flashcards.dto.FavoriteDto;
import com.flashcards.dto.WordDto;
import com.flashcards.entity.*;
import com.flashcards.entity.Favorite.FavoriteType;
import com.flashcards.exception.FlashcardExceptions;
import com.flashcards.mapper.FavoriteMapper;
import com.flashcards.mapper.WordMapper;
import com.flashcards.mapper.CollectionMapper;
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
    private final FavoriteMapper favoriteMapper;
    private final WordMapper wordMapper;
    private final CollectionMapper collectionMapper;

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
        return favoriteMapper.toDto(favorite);
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
        return favoriteMapper.toDto(favorite);
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
                .map(favoriteMapper::toDto)
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

    // Convert to DTO with details
    private FavoriteDto convertToDtoWithDetails(Favorite favorite) {
        FavoriteDto dto = favoriteMapper.toDto(favorite);

        if (favorite.getWord() != null) {
            dto.setWord(wordMapper.toDto(favorite.getWord()));
        }
        if (favorite.getCollection() != null) {
            dto.setCollection(collectionMapper.toDto(favorite.getCollection()));
        }

        return dto;
    }
}