package com.flashcards.mapper;

import com.flashcards.dto.NotificationScheduleDto;
import com.flashcards.entity.NotificationSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationScheduleMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "wordId", source = "word.id")
    @Mapping(target = "wordEnglishWord", source = "word.word")
    @Mapping(target = "wordVietnameseMeaning", ignore = true)
    @Mapping(target = "wordUsageContext", ignore = true)
    @Mapping(target = "wordExampleSentence", ignore = true)
    NotificationScheduleDto toDto(NotificationSchedule notification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isSent", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "word", ignore = true)
    NotificationSchedule toEntity(NotificationScheduleDto dto);
}
