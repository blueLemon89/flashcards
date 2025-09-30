package com.flashcards.controller;

import com.flashcards.dto.WordDto;
import com.flashcards.dto.response.ApiResponse;
import com.flashcards.service.DictionaryApiService;
import com.flashcards.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WordController {
    private final WordService wordService;
    private final DictionaryApiService dictionaryApiService;

    @GetMapping("/collection/{collectionId}")
    public ResponseEntity<List<WordDto>> getWordsByCollection(@PathVariable Long collectionId) {
        List<WordDto> words = wordService.getAllWordsByCollection(collectionId);
        return ResponseEntity.ok(words);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WordDto>> getWordsByUser(@PathVariable Long userId) {
        List<WordDto> words = wordService.getAllWordsByUser(userId);
        return ResponseEntity.ok(words);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WordDto> getWordById(@PathVariable Long id) {
        WordDto word = wordService.getWordById(id);
        return ResponseEntity.ok(word);
    }

    @PostMapping
    public ResponseEntity<WordDto> createWord(@Valid @RequestBody WordDto wordDto) {
        WordDto createdWord = wordService.createWord(wordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordDto> updateWord(@PathVariable Long id, @Valid @RequestBody WordDto wordDto) {
        WordDto updatedWord = wordService.updateWord(id, wordDto);
        return ResponseEntity.ok(updatedWord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<WordDto>> searchWords(@RequestParam String keyword) {
        List<WordDto> words = wordService.searchWords(keyword);
        return ResponseEntity.ok(words);
    }
}