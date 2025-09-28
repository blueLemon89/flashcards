package com.flashcards.repository;

import com.flashcards.entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, Long> {
    List<NotificationSchedule> findByUserIdAndIsActiveTrue(Long userId);

    @Query("SELECT ns FROM NotificationSchedule ns WHERE ns.scheduledTime <= :currentTime AND ns.isSent = false AND ns.isActive = true")
    List<NotificationSchedule> findPendingNotifications(@Param("currentTime") LocalDateTime currentTime);

    List<NotificationSchedule> findByFlashcardIdAndIsActiveTrue(Long flashcardId);
}