package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.EventStatDto;
import ru.practicum.model.EventStat;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventStatMapper {

    EventStatMapper INSTANCE = Mappers.getMapper(EventStatMapper.class);

    EventStatDto eventStatToEventStatDto(EventStat eventStat);

    EventStat eventStatDtoToEventStat(EventStatDto eventStatDto);

    List<EventStatDto> sourceListToTargetList(List<EventStat> sourceList);

    List<EventStat> targetListToSourceList(List<EventStatDto> sourceList);


}
