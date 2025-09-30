package com.flashcards.service;

import com.flashcards.dto.*;
import com.flashcards.entity.Collection;
import com.flashcards.entity.Word;
import com.flashcards.repository.CollectionRepository;
import com.flashcards.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryApiService {
    private final WordRepository wordRepository;
    private final CollectionRepository collectionRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.dictionary.search}")
    private String dictionaryApiUrl;

    @Transactional
    public WordDto fetchAndSaveWord(String wordText, Long collectionId) {
        try {
            // Check if word already exists
            if (wordRepository.existsByWord(wordText)) {
                throw new RuntimeException("Word already exists: " + wordText);
            }

            Collection collection = collectionRepository.findById(collectionId)
                    .orElseThrow(() -> new RuntimeException("Collection not found"));

            // Fetch from API
            String url = dictionaryApiUrl + wordText;
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);

            if (response == null || response.length == 0) {
                throw new RuntimeException("No data found for word: " + wordText);
            }

            Word word = parseApiResponse(response[0], collection);
            word = wordRepository.save(word);

            log.info("Successfully saved word: {}", wordText);
            return convertToDto(word);

        } catch (Exception e) {
            log.error("Error fetching word from API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch word: " + wordText + " - " + e.getMessage());
        }
    }

    @Transactional
    public List<WordDto> fetchAndSaveWords(List<String> wordTexts, Long collectionId) {
        List<WordDto> savedWords = new ArrayList<>();

        for (String wordText : wordTexts) {
            try {
                WordDto wordDto = fetchAndSaveWord(wordText.trim().toLowerCase(), collectionId);
                savedWords.add(wordDto);
            } catch (Exception e) {
                log.warn("Skipping word '{}': {}", wordText, e.getMessage());
            }
        }

        return savedWords;
    }

    private Word parseApiResponse(Map<String, Object> apiData, Collection collection) {
        Word word = new Word();
        word.setWord((String) apiData.get("word"));
        word.setPhonetic((String) apiData.get("phonetic"));
        word.setCollection(collection);

        // Parse phonetics
        List<Map<String, Object>> phonetics = (List<Map<String, Object>>) apiData.get("phonetics");
        if (phonetics != null) {
            for (Map<String, Object> phoneticData : phonetics) {
                if (phoneticData.get("audio") != null && !phoneticData.get("audio").toString().isEmpty()) {
                    word.setAudioUrl((String) phoneticData.get("audio"));
                    word.setSourceUrl((String) phoneticData.get("sourceUrl"));
                    break;
                }
            }
        }

        // Parse meanings
        List<Map<String, Object>> meanings = (List<Map<String, Object>>) apiData.get("meanings");
        if (meanings != null) {
            for (Map<String, Object> meaningData : meanings) {
                com.flashcards.entity.WordMeaning meaning = new com.flashcards.entity.WordMeaning();
                meaning.setPartOfSpeech((String) meaningData.get("partOfSpeech"));
                meaning.setWord(word);

                // Synonyms & Antonyms
                List<String> synonyms = (List<String>) meaningData.get("synonyms");
                List<String> antonyms = (List<String>) meaningData.get("antonyms");
                if (synonyms != null) meaning.setSynonyms(synonyms);
                if (antonyms != null) meaning.setAntonyms(antonyms);

                // Parse definitions
                List<Map<String, Object>> definitions = (List<Map<String, Object>>) meaningData.get("definitions");
                if (definitions != null) {
                    for (Map<String, Object> defData : definitions) {
                        com.flashcards.entity.WordDefinition definition = new com.flashcards.entity.WordDefinition();
                        definition.setDefinition((String) defData.get("definition"));
                        definition.setExample((String) defData.get("example"));
                        definition.setMeaning(meaning);

                        List<String> defSynonyms = (List<String>) defData.get("synonyms");
                        List<String> defAntonyms = (List<String>) defData.get("antonyms");
                        if (defSynonyms != null) definition.setSynonyms(defSynonyms);
                        if (defAntonyms != null) definition.setAntonyms(defAntonyms);

                        meaning.getDefinitions().add(definition);
                    }
                }

                word.getMeanings().add(meaning);
            }
        }

        return word;
    }

    private WordDto convertToDto(Word word) {
        WordDto dto = new WordDto();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setPhonetic(word.getPhonetic());
        dto.setAudioUrl(word.getAudioUrl());
        dto.setSourceUrl(word.getSourceUrl());
        dto.setDifficultyLevel(word.getDifficultyLevel());
        dto.setCustomNotificationInterval(word.getCustomNotificationInterval());
        dto.setCollectionId(word.getCollection().getId());
        dto.setCollectionName(word.getCollection().getName());
        dto.setIsActive(word.getIsActive());
        dto.setCreatedAt(word.getCreatedAt());
        dto.setUpdatedAt(word.getUpdatedAt());
        return dto;
    }
}
