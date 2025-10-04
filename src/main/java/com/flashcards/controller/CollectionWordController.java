package com.flashcards.controller;

import com.flashcards.dto.CollectionWordDto;
import com.flashcards.dto.response.ApiResponse;
import com.flashcards.service.CollectionWordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections/{collectionId}/words")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CollectionWordController {
    private final CollectionWordService collectionWordService;

    /**
     * Get all words in a collection
     */
    @GetMapping
    public ResponseEntity<List<CollectionWordDto>> getWordsInCollection(@PathVariable Long collectionId) {
        List<CollectionWordDto> words = collectionWordService.getWordsByCollection(collectionId);
        return ResponseEntity.ok(words);
    }

    /**
     * Add a word to collection
     */
    @PostMapping("/{wordId}")
    public ResponseEntity<ApiResponse<CollectionWordDto>> addWordToCollection(
            @PathVariable Long collectionId,
            @PathVariable Long wordId) {
        CollectionWordDto dto = collectionWordService.addWordToCollection(collectionId, wordId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(dto));
    }

    /**
     * Update word settings in collection (difficulty, notes, etc.)
     */
    @PutMapping("/{wordId}")
    public ResponseEntity<ApiResponse<CollectionWordDto>> updateWordInCollection(
            @PathVariable Long collectionId,
            @PathVariable Long wordId,
            @Valid @RequestBody CollectionWordDto dto) {
        CollectionWordDto updated = collectionWordService.updateCollectionWord(collectionId, wordId, dto);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    /**
     * Remove word from collection
     */
    @DeleteMapping("/{wordId}")
    public ResponseEntity<Void> removeWordFromCollection(
            @PathVariable Long collectionId,
            @PathVariable Long wordId) {
        collectionWordService.removeWordFromCollection(collectionId, wordId);
        return ResponseEntity.noContent().build();
    }
}
