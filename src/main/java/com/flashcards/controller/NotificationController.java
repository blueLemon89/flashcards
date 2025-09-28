package com.flashcards.controller;

import com.flashcards.dto.NotificationScheduleDto;
import com.flashcards.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationScheduleDto>> getNotificationsByUser(@PathVariable Long userId) {
        List<NotificationScheduleDto> notifications = notificationService.getAllNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<NotificationScheduleDto> scheduleNotification(@Valid @RequestBody NotificationScheduleDto notificationDto) {
        NotificationScheduleDto scheduledNotification = notificationService.scheduleNotification(notificationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduledNotification);
    }

    @PostMapping("/flashcard/{flashcardId}/user/{userId}")
    public ResponseEntity<Void> scheduleNotificationForFlashcard(@PathVariable Long flashcardId, @PathVariable Long userId) {
        notificationService.scheduleNotificationForFlashcard(flashcardId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> cancelNotification(@PathVariable Long notificationId) {
        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<NotificationScheduleDto>> getPendingNotifications() {
        List<NotificationScheduleDto> pendingNotifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(pendingNotifications);
    }
}