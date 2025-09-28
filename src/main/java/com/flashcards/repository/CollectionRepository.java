package com.flashcards.repository;

import com.flashcards.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserIdAndIsActiveTrue(Long userId);
    List<Collection> findByUserId(Long userId);
}