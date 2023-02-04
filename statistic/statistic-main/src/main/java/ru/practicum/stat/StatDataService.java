package ru.practicum.stat;

import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticDtoIn;
import ru.practicum.stat.model.StatData;

import java.util.Collection;

@Service
public interface StatDataService {

    Collection<StatisticDto> getStats(String start, String end, String[] uris, boolean unique);

    StatData postStat(StatisticDtoIn statistic);
}
