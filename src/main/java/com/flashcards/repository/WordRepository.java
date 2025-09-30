package com.flashcards.repository;

import com.flashcards.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWord(String word);
    boolean existsByWord(String word);

    List<Word> findByCollectionIdAndIsActiveTrue(Long collectionId);
    List<Word> findByCollectionId(Long collectionId);

    @Query("SELECT w FROM Word w WHERE w.collection.user.id = :userId AND w.isActive = true")
    List<Word> findByUserIdAndIsActiveTrue(@Param("userId") Long userId);

    @Query("SELECT w FROM Word w WHERE w.collection.user.id = :userId")
    List<Word> findByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM Word w WHERE LOWER(w.word) LIKE LOWER(CONCAT('%', :keyword, '%')) AND w.isActive = true")
    List<Word> findByWordContainingIgnoreCaseAndIsActiveTrue(@Param("keyword") String keyword);
}