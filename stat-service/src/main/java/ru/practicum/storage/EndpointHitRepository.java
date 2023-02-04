package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    String query = "SELECT app, uri, count(*) FROM endpoint_hit  " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            " group by app, uri order by  count(*) desc ";


    List<EndpointHit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = query, nativeQuery = true)
    List<EndpointHit> findAllByUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT app, uri, count(*) FROM endpoint_hit  " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND uri IN(?3) " +
            " group by app, uri order by  count(*) desc ", nativeQuery = true)
    List<EndpointHit> findAllByUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<EndpointHit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT uri, COUNT(*) AS count_hited " +
            " FROM endpoint_hit " +
            " WHERE uri IN ?1 " +
            "GROUP BY uri " +
            "order by COUNT(*) desc",
            nativeQuery = true)
    List<Object[]> getCountHitByUriList(List<String> uris);

}
