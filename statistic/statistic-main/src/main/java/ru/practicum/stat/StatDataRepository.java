package ru.practicum.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatisticDto;
import ru.practicum.stat.model.StatData;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface StatDataRepository extends JpaRepository<StatData, Long> {

    @Query(value = "select new ru.practicum.dto.StatisticDto(s.app, s.uri, count(s.ip)) " +
            "from StatData  as s " +
            "WHERE s.uri in ?3 " +
            "and s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.app) desc ")
    Collection<StatisticDto> findBetweenStartAndEndByUrisNonUnique(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "select new ru.practicum.dto.StatisticDto(s.app, s.uri, count(distinct s.ip)) " +
            "from StatData as s " +
            "WHERE s.uri in ?3 " +
            "and s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.app) desc ")
    Collection<StatisticDto> findBetweenStartAndEndByUrisUnique(LocalDateTime start, LocalDateTime end, String[] uris);
}
