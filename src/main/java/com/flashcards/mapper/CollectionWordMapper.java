package com.flashcards.mapper;

import com.flashcards.dto.CollectionWordDto;
import com.flashcards.entity.CollectionWord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CollectionWordMapper {

    @Mapping(target = "collectionId", source = "collection.id")
    @Mapping(target = "wordId", source = "word.id")
    @Mapping(target = "word", source = "word.word")
    @Mapping(target = "phonetic", source = "word.phonetic")
    @Mapping(target = "audioUrl", source = "word.audioUrl")
    CollectionWordDto toDto(CollectionWord collectionWord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "collection", ignore = true)
    @Mapping(target = "word", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CollectionWord toEntity(CollectionWordDto dto);
}
