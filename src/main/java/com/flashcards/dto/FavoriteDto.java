package com.flashcards.dto;

import com.flashcards.entity.Favorite.FavoriteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {
    private Long id;
    private Long userId;
    private Long wordId;
    private Long collectionId;
    private FavoriteType favoriteType;
    private LocalDateTime createdAt;

    // Thông tin chi tiết của word (nếu có)
    private WordDto word;

    // Thông tin chi tiết của collection (nếu có)
    private CollectionDto collection;
}