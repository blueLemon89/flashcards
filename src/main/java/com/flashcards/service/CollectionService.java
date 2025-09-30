package com.flashcards.service;

import com.flashcards.dto.CollectionDto;
import com.flashcards.entity.Collection;
import com.flashcards.entity.User;
import com.flashcards.repository.CollectionRepository;
import com.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    public List<CollectionDto> getAllCollectionsByUser(Long userId) {
        List<Collection> collections = collectionRepository.findByUserIdAndIsActiveTrue(userId);
        return collections.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CollectionDto getCollectionById(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));
        return convertToDto(collection);
    }

    public CollectionDto createCollection(CollectionDto collectionDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Collection collection = convertToEntity(collectionDto);
        collection.setUser(user);
        collection = collectionRepository.save(collection);
        return convertToDto(collection);
    }

    public CollectionDto updateCollection(Long id, CollectionDto collectionDto) {
        Collection existingCollection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        existingCollection.setName(collectionDto.getName());
        existingCollection.setDescription(collectionDto.getDescription());

        existingCollection = collectionRepository.save(existingCollection);
        return convertToDto(existingCollection);
    }

    public void deleteCollection(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));
        collection.setIsActive(false);
        collectionRepository.save(collection);
    }

    private CollectionDto convertToDto(Collection collection) {
        CollectionDto dto = new CollectionDto();
        dto.setId(collection.getId());
        dto.setName(collection.getName());
        dto.setDescription(collection.getDescription());
        dto.setIsActive(collection.getIsActive());
        dto.setCreatedAt(collection.getCreatedAt());
        dto.setUpdatedAt(collection.getUpdatedAt());
        dto.setUserId(collection.getUser().getId());
        dto.setUserName(collection.getUser().getUsername());

        if (collection.getWords() != null) {
            dto.setWordCount(collection.getWords().size());
        } else {
            dto.setWordCount(0);
        }

        return dto;
    }

    private Collection convertToEntity(CollectionDto dto) {
        Collection collection = new Collection();
        collection.setName(dto.getName());
        collection.setDescription(dto.getDescription());
        return collection;
    }
}