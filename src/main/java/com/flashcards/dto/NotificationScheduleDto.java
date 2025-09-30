package com.flashcards.dto;

import com.flashcards.entity.NotificationSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationScheduleDto {
    private Long id;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledTime;

    private NotificationSchedule.NotificationType notificationType;
    private Boolean isSent;
    private LocalDateTime sentAt;
    private Boolean isActive;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Word ID is required")
    private Long wordId;

    private String wordEnglishWord;
    private String wordVietnameseMeaning;
    private String wordUsageContext;
    private String wordExampleSentence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}