package com.flashcards.repository;

import com.flashcards.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByCollectionIdAndIsActiveTrue(Long collectionId);
    List<Flashcard> findByCollectionId(Long collectionId);

    @Query("SELECT f FROM Flashcard f WHERE f.collection.user.id = :userId AND f.isActive = true")
    List<Flashcard> findByUserIdAndIsActiveTrue(@Param("userId") Long userId);

    @Query("SELECT f FROM Flashcard f WHERE f.collection.user.id = :userId")
    List<Flashcard> findByUserId(@Param("userId") Long userId);

    List<Flashcard> findByEnglishWordContainingIgnoreCaseAndIsActiveTrue(String keyword);
}