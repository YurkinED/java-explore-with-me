package ru.practicum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EventStat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventStat, Integer> {
    @Query(value = "select e.uri as uri, e.app as app, sum(1) as hits from events as e " +
            " where e.uri in (:uris) and e.created between :start and :end" +
            " group by e.uri, e.app" +
            " order by hits desc ",
            nativeQuery = true)
    List<EventHits> findAllStatsWithFilter(@Param("uris") List<String> listUris,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end

    );
}
