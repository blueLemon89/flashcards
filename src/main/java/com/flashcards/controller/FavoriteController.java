package com.flashcards.controller;

import com.flashcards.dto.FavoriteDto;
import com.flashcards.dto.response.ApiResponse;
import com.flashcards.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FavoriteController {
    private final FavoriteService favoriteService;

    // Thêm word vào favorites
    @PostMapping("/words/{wordId}")
    public ResponseEntity<ApiResponse<FavoriteDto>> addWordToFavorites(
            @PathVariable Long wordId,
            @RequestParam Long userId) {
        FavoriteDto favorite = favoriteService.addWordToFavorites(userId, wordId);
        return ResponseEntity.ok(ApiResponse.success("Word đã được thêm vào favorites", favorite));
    }

    // Thêm collection vào favorites
    @PostMapping("/collections/{collectionId}")
    public ResponseEntity<ApiResponse<FavoriteDto>> addCollectionToFavorites(
            @PathVariable Long collectionId,
            @RequestParam Long userId) {
        FavoriteDto favorite = favoriteService.addCollectionToFavorites(userId, collectionId);
        return ResponseEntity.ok(ApiResponse.success("Collection đã được thêm vào favorites", favorite));
    }

    // Xóa word khỏi favorites
    @DeleteMapping("/words/{wordId}")
    public ResponseEntity<ApiResponse<Void>> removeWordFromFavorites(
            @PathVariable Long wordId,
            @RequestParam Long userId) {
        favoriteService.removeWordFromFavorites(userId, wordId);
        return ResponseEntity.ok(ApiResponse.success("Word đã được xóa khỏi favorites", null));
    }

    // Xóa collection khỏi favorites
    @DeleteMapping("/collections/{collectionId}")
    public ResponseEntity<ApiResponse<Void>> removeCollectionFromFavorites(
            @PathVariable Long collectionId,
            @RequestParam Long userId) {
        favoriteService.removeCollectionFromFavorites(userId, collectionId);
        return ResponseEntity.ok(ApiResponse.success("Collection đã được xóa khỏi favorites", null));
    }

    // Lấy tất cả favorite words của user
    @GetMapping("/words")
    public ResponseEntity<ApiResponse<List<FavoriteDto>>> getFavoriteWords(
            @RequestParam Long userId) {
        List<FavoriteDto> favorites = favoriteService.getFavoriteWords(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    // Lấy tất cả favorite collections của user
    @GetMapping("/collections")
    public ResponseEntity<ApiResponse<List<FavoriteDto>>> getFavoriteCollections(
            @RequestParam Long userId) {
        List<FavoriteDto> favorites = favoriteService.getFavoriteCollections(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    // Lấy tất cả favorites của user (cả words và collections)
    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteDto>>> getAllFavorites(
            @RequestParam Long userId) {
        List<FavoriteDto> favorites = favoriteService.getAllFavorites(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    // Kiểm tra word có phải favorite không
    @GetMapping("/words/{wordId}/check")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkWordFavorite(
            @PathVariable Long wordId,
            @RequestParam Long userId) {
        boolean isFavorite = favoriteService.isWordFavorite(userId, wordId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("isFavorite", isFavorite)));
    }

    // Kiểm tra collection có phải favorite không
    @GetMapping("/collections/{collectionId}/check")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkCollectionFavorite(
            @PathVariable Long collectionId,
            @RequestParam Long userId) {
        boolean isFavorite = favoriteService.isCollectionFavorite(userId, collectionId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("isFavorite", isFavorite)));
    }
}