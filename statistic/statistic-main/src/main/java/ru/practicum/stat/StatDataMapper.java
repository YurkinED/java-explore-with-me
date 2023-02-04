package ru.practicum.stat;

import org.mapstruct.Mapper;
import ru.practicum.dto.StatisticDtoIn;
import ru.practicum.stat.model.StatData;

@Mapper(componentModel = "spring")
public interface StatDataMapper {
    StatData convertDtoToStatistic(StatisticDtoIn statisticDtoIn);
}

