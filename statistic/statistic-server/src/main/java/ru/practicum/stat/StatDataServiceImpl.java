package ru.practicum.stat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticDtoIn;
import ru.practicum.stat.model.StatData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatDataServiceImpl implements StatDataService {
    private final StatDataRepository statDataRepository;
    private final StatDataMapper statDataMapper;

    @Override
    public Collection<StatisticDto> getStats(String start, String end, String[] uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime starTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        if (unique) {
            return statDataRepository.findBetweenStartAndEndByUrisUnique(starTime, endTime, uris);
        } else {
            return statDataRepository.findBetweenStartAndEndByUrisNonUnique(starTime, endTime, uris);
        }
    }

    @Override
    public StatData postStat(StatisticDtoIn statistic) {
        return statDataRepository.save(statDataMapper.convertDtoToStatistic(statistic));
    }
}
