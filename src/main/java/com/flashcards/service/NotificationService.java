package com.flashcards.service;

import com.flashcards.dto.NotificationScheduleDto;
import com.flashcards.entity.Flashcard;
import com.flashcards.entity.NotificationSchedule;
import com.flashcards.entity.User;
import com.flashcards.repository.FlashcardRepository;
import com.flashcards.repository.NotificationScheduleRepository;
import com.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationScheduleRepository notificationScheduleRepository;
    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;

    public List<NotificationScheduleDto> getAllNotificationsByUser(Long userId) {
        List<NotificationSchedule> notifications = notificationScheduleRepository.findByUserIdAndIsActiveTrue(userId);
        return notifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public NotificationScheduleDto scheduleNotification(NotificationScheduleDto notificationDto) {
        User user = userRepository.findById(notificationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Flashcard flashcard = flashcardRepository.findById(notificationDto.getFlashcardId())
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        NotificationSchedule notification = convertToEntity(notificationDto);
        notification.setUser(user);
        notification.setFlashcard(flashcard);

        notification = notificationScheduleRepository.save(notification);
        return convertToDto(notification);
    }

    public void scheduleNotificationForFlashcard(Long flashcardId, Long userId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int intervalMinutes = flashcard.getCustomNotificationInterval() != null
            ? flashcard.getCustomNotificationInterval()
            : user.getDefaultNotificationInterval();

        NotificationSchedule notification = new NotificationSchedule();
        notification.setUser(user);
        notification.setFlashcard(flashcard);
        notification.setScheduledTime(LocalDateTime.now().plusMinutes(intervalMinutes));
        notification.setNotificationType(NotificationSchedule.NotificationType.VOCABULARY_REVIEW);

        notificationScheduleRepository.save(notification);
    }

    @Scheduled(fixedRate = 60000) // Check every minute
    public void processPendingNotifications() {
        List<NotificationSchedule> pendingNotifications =
            notificationScheduleRepository.findPendingNotifications(LocalDateTime.now());

        for (NotificationSchedule notification : pendingNotifications) {
            try {
                sendNotification(notification);
                notification.setIsSent(true);
                notification.setSentAt(LocalDateTime.now());
                notificationScheduleRepository.save(notification);
            } catch (Exception e) {
                log.error("Failed to send notification: {}", e.getMessage(), e);
            }
        }
    }

    private void sendNotification(NotificationSchedule notification) {
        log.info("Sending notification to user: {}", notification.getUser().getUsername());
        log.info("Word: {}", notification.getFlashcard().getEnglishWord());
        log.info("Meaning: {}", notification.getFlashcard().getVietnameseMeaning());
        log.info("Usage: {}", notification.getFlashcard().getUsageContext());
        log.info("Example: {}", notification.getFlashcard().getExampleSentenceEnglish());
    }

    public void cancelNotification(Long notificationId) {
        NotificationSchedule notification = notificationScheduleRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsActive(false);
        notificationScheduleRepository.save(notification);
    }

    public List<NotificationScheduleDto> getPendingNotifications() {
        List<NotificationSchedule> pendingNotifications =
            notificationScheduleRepository.findPendingNotifications(LocalDateTime.now());
        return pendingNotifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private NotificationScheduleDto convertToDto(NotificationSchedule notification) {
        NotificationScheduleDto dto = new NotificationScheduleDto();
        dto.setId(notification.getId());
        dto.setScheduledTime(notification.getScheduledTime());
        dto.setNotificationType(notification.getNotificationType());
        dto.setIsSent(notification.getIsSent());
        dto.setSentAt(notification.getSentAt());
        dto.setIsActive(notification.getIsActive());
        dto.setUserId(notification.getUser().getId());
        dto.setFlashcardId(notification.getFlashcard().getId());
        dto.setFlashcardEnglishWord(notification.getFlashcard().getEnglishWord());
        dto.setFlashcardVietnameseMeaning(notification.getFlashcard().getVietnameseMeaning());
        dto.setFlashcardUsageContext(notification.getFlashcard().getUsageContext());
        dto.setFlashcardExampleSentence(notification.getFlashcard().getExampleSentenceEnglish());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        return dto;
    }

    private NotificationSchedule convertToEntity(NotificationScheduleDto dto) {
        NotificationSchedule notification = new NotificationSchedule();
        notification.setScheduledTime(dto.getScheduledTime());
        notification.setNotificationType(dto.getNotificationType());
        return notification;
    }
}