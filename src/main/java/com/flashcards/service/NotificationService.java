package com.flashcards.service;

import com.flashcards.dto.NotificationScheduleDto;
import com.flashcards.entity.*;
import com.flashcards.repository.NotificationScheduleRepository;
import com.flashcards.repository.UserRepository;
import com.flashcards.repository.WordRepository;
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
    private final WordRepository wordRepository;

    public List<NotificationScheduleDto> getAllNotificationsByUser(Long userId) {
        List<NotificationSchedule> notifications = notificationScheduleRepository.findByUserIdAndIsActiveTrue(userId);
        return notifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public NotificationScheduleDto scheduleNotification(NotificationScheduleDto notificationDto) {
        User user = userRepository.findById(notificationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Word word = wordRepository.findById(notificationDto.getWordId())
                .orElseThrow(() -> new RuntimeException("Word not found"));

        NotificationSchedule notification = convertToEntity(notificationDto);
        notification.setUser(user);
        notification.setWord(word);

        notification = notificationScheduleRepository.save(notification);
        return convertToDto(notification);
    }

    public void scheduleNotificationForWord(Long wordId, Long userId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int intervalMinutes = word.getCustomNotificationInterval() != null
            ? word.getCustomNotificationInterval()
            : user.getDefaultNotificationInterval();

        NotificationSchedule notification = new NotificationSchedule();
        notification.setUser(user);
        notification.setWord(word);
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
        log.info("Word: {}", notification.getWord().getWord());
        log.info("Phonetic: {}", notification.getWord().getPhonetic());
        log.info("Audio: {}", notification.getWord().getAudioUrl());

        // Log first meaning if available
        if (!notification.getWord().getMeanings().isEmpty()) {
            WordMeaning firstMeaning = notification.getWord().getMeanings().get(0);
            log.info("Part of speech: {}", firstMeaning.getPartOfSpeech());
            if (!firstMeaning.getDefinitions().isEmpty()) {
                WordDefinition firstDef = firstMeaning.getDefinitions().get(0);
                log.info("Definition: {}", firstDef.getDefinition());
                log.info("Example: {}", firstDef.getExample());
            }
        }
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
        dto.setWordId(notification.getWord().getId());
        dto.setWordEnglishWord(notification.getWord().getWord());

        // Get first meaning and definition if available
        String meaning = "";
        String context = "";
        String example = "";

        if (!notification.getWord().getMeanings().isEmpty()) {
            WordMeaning firstMeaning = notification.getWord().getMeanings().get(0);
            if (!firstMeaning.getDefinitions().isEmpty()) {
                WordDefinition firstDef = firstMeaning.getDefinitions().get(0);
                meaning = firstDef.getDefinition();
                context = firstMeaning.getPartOfSpeech();
                example = firstDef.getExample() != null ? firstDef.getExample() : "";
            }
        }

        dto.setWordVietnameseMeaning(meaning);
        dto.setWordUsageContext(context);
        dto.setWordExampleSentence(example);
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