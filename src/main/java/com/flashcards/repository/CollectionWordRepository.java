package com.flashcards.repository;

import com.flashcards.entity.CollectionWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionWordRepository extends JpaRepository<CollectionWord, Long> {

    List<CollectionWord> findByCollectionIdAndIsActiveTrue(Long collectionId);

    List<CollectionWord> findByCollectionId(Long collectionId);

    Optional<CollectionWord> findByCollectionIdAndWordId(Long collectionId, Long wordId);

    boolean existsByCollectionIdAndWordId(Long collectionId, Long wordId);

    @Query("SELECT cw FROM CollectionWord cw WHERE cw.collection.user.id = :userId AND cw.isActive = true")
    List<CollectionWord> findByUserIdAndIsActiveTrue(@Param("userId") Long userId);

    @Query("SELECT COUNT(cw) FROM CollectionWord cw WHERE cw.collection.id = :collectionId AND cw.isActive = true")
    Long countByCollectionIdAndIsActiveTrue(@Param("collectionId") Long collectionId);

    void deleteByCollectionIdAndWordId(Long collectionId, Long wordId);
}
