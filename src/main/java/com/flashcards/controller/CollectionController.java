package com.flashcards.controller;

import com.flashcards.dto.CollectionDto;
import com.flashcards.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollectionDto>> getCollectionsByUser(@PathVariable Long userId) {
        List<CollectionDto> collections = collectionService.getAllCollectionsByUser(userId);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> getCollectionById(@PathVariable Long id) {
        CollectionDto collection = collectionService.getCollectionById(id);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<CollectionDto> createCollection(@PathVariable Long userId, @Valid @RequestBody CollectionDto collectionDto) {
        CollectionDto createdCollection = collectionService.createCollection(collectionDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCollection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionDto> updateCollection(@PathVariable Long id, @Valid @RequestBody CollectionDto collectionDto) {
        CollectionDto updatedCollection = collectionService.updateCollection(id, collectionDto);
        return ResponseEntity.ok(updatedCollection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }
}