package com.flashcards.mapper;

import com.flashcards.dto.CollectionDto;
import com.flashcards.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CollectionMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "wordCount", ignore = true)
    @Mapping(target = "words", ignore = true)
    CollectionDto toDto(Collection collection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "collectionWords", ignore = true)
    Collection toEntity(CollectionDto dto);
}
