package com.flashcards.controller;

import com.flashcards.dto.FlashcardDto;
import com.flashcards.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FlashcardController {
    private final FlashcardService flashcardService;

    @GetMapping("/collection/{collectionId}")
    public ResponseEntity<List<FlashcardDto>> getFlashcardsByCollection(@PathVariable Long collectionId) {
        List<FlashcardDto> flashcards = flashcardService.getAllFlashcardsByCollection(collectionId);
        return ResponseEntity.ok(flashcards);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FlashcardDto>> getFlashcardsByUser(@PathVariable Long userId) {
        List<FlashcardDto> flashcards = flashcardService.getAllFlashcardsByUser(userId);
        return ResponseEntity.ok(flashcards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardDto> getFlashcardById(@PathVariable Long id) {
        FlashcardDto flashcard = flashcardService.getFlashcardById(id);
        return ResponseEntity.ok(flashcard);
    }

    @PostMapping
    public ResponseEntity<FlashcardDto> createFlashcard(@Valid @RequestBody FlashcardDto flashcardDto) {
        FlashcardDto createdFlashcard = flashcardService.createFlashcard(flashcardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlashcard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardDto> updateFlashcard(@PathVariable Long id, @Valid @RequestBody FlashcardDto flashcardDto) {
        FlashcardDto updatedFlashcard = flashcardService.updateFlashcard(id, flashcardDto);
        return ResponseEntity.ok(updatedFlashcard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlashcardDto>> searchFlashcards(@RequestParam String keyword) {
        List<FlashcardDto> flashcards = flashcardService.searchFlashcards(keyword);
        return ResponseEntity.ok(flashcards);
    }
}