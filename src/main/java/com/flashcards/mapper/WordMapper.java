package com.flashcards.mapper;

import com.flashcards.dto.WordDefinitionDto;
import com.flashcards.dto.WordDto;
import com.flashcards.dto.WordMeaningDto;
import com.flashcards.dto.WordPhoneticDto;
import com.flashcards.entity.Word;
import com.flashcards.entity.WordDefinition;
import com.flashcards.entity.WordMeaning;
import com.flashcards.entity.WordPhonetic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WordMapper {

    WordDto toDto(Word word);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "phonetics", ignore = true)
    @Mapping(target = "meanings", ignore = true)
    @Mapping(target = "notificationSchedules", ignore = true)
    Word toEntity(WordDto dto);

    // WordPhonetic mappings
    @Mapping(target = "word", ignore = true)
    WordPhonetic toEntity(WordPhoneticDto dto);

    WordPhoneticDto toDto(WordPhonetic phonetic);

    // WordMeaning mappings
    @Mapping(target = "word", ignore = true)
    WordMeaning toEntity(WordMeaningDto dto);

    WordMeaningDto toDto(WordMeaning meaning);

    // WordDefinition mappings
    @Mapping(target = "meaning", ignore = true)
    WordDefinition toEntity(WordDefinitionDto dto);

    WordDefinitionDto toDto(WordDefinition definition);
}
