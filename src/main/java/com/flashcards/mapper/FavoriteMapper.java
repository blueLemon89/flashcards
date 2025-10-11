package com.flashcards.mapper;

import com.flashcards.dto.FavoriteDto;
import com.flashcards.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {WordMapper.class, CollectionMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "wordId", source = "word.id")
    @Mapping(target = "collectionId", source = "collection.id")
    @Mapping(target = "word", ignore = true)
    @Mapping(target = "collection", ignore = true)
    FavoriteDto toDto(Favorite favorite);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "word", ignore = true)
    @Mapping(target = "collection", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Favorite toEntity(FavoriteDto dto);
}
