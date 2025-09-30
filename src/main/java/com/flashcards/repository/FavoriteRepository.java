package com.flashcards.repository;

import com.flashcards.entity.Favorite;
import com.flashcards.entity.Favorite.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Tìm favorite của user cho một word
    Optional<Favorite> findByUserIdAndWordId(Long userId, Long wordId);

    // Tìm favorite của user cho một collection
    Optional<Favorite> findByUserIdAndCollectionId(Long userId, Long collectionId);

    // Kiểm tra word có phải favorite không
    boolean existsByUserIdAndWordId(Long userId, Long wordId);

    // Kiểm tra collection có phải favorite không
    boolean existsByUserIdAndCollectionId(Long userId, Long collectionId);

    // Lấy tất cả favorites của user theo type
    List<Favorite> findByUserIdAndFavoriteType(Long userId, FavoriteType favoriteType);

    // Lấy tất cả favorites của user
    List<Favorite> findByUserId(Long userId);

    // Lấy tất cả favorite words của user với thông tin đầy đủ
    @Query("SELECT f FROM Favorite f JOIN FETCH f.word WHERE f.user.id = :userId AND f.favoriteType = 'WORD'")
    List<Favorite> findAllFavoriteWordsByUserId(@Param("userId") Long userId);

    // Lấy tất cả favorite collections của user với thông tin đầy đủ
    @Query("SELECT f FROM Favorite f JOIN FETCH f.collection WHERE f.user.id = :userId AND f.favoriteType = 'COLLECTION'")
    List<Favorite> findAllFavoriteCollectionsByUserId(@Param("userId") Long userId);

    // Xóa favorite của user cho word
    void deleteByUserIdAndWordId(Long userId, Long wordId);

    // Xóa favorite của user cho collection
    void deleteByUserIdAndCollectionId(Long userId, Long collectionId);
}