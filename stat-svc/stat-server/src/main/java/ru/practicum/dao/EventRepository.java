package ru.practicum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStats;
import ru.practicum.model.EventStat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventStat, Integer> {
    @Query(value = "select NEW ru.practicum.ViewStats(e.uri, e.app, COUNT(e.ip))  " +
            "from EventStat e " +
            " where (e.uri in (:uris) or :uris is null)" +
            "and e.created between :start and :end" +
            " group by e.uri, e.app" +
            " order by COUNT(e.ip) desc ")
    List<ViewStats> findAllStatsWithFilter(@Param("uris") List<String> listUris,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query(value = "select NEW ru.practicum.ViewStats(e.uri, e.app, COUNT(DISTINCT e.ip))  " +
            "from EventStat e " +
            " where (e.uri in (:uris) or :uris is null) " +
            " and e.created between :start and :end" +
            " group by e.uri, e.app " +
            " order by COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findDistinctAllStatsWithFilter(@Param("uris") List<String> listUris,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);
}
