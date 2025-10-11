package com.flashcards.repository;

import com.flashcards.entity.RefreshToken;
import com.flashcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByToken(String token);

    Optional<RefreshToken> findByUserAndIsRevokedFalse(User user);
}
