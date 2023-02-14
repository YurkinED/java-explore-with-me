package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.TypeStateRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Collection<Request> findByRequesterId(Long userId);

    @Query(value = "select r " +
            "from Request as r " +
            "WHERE  r.event.initiator.id = ?1 " +
            "And r.event.id = ?2 ")
    Collection<Request> findByEventIdAndUser(Long userId, Long eventId);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    Collection<Request> findByIdIn(Long[] requestIds);

    Collection<Request> findByEventIdIn(List<Event> eventId);

    Collection<Request> findByEventId(Long eventId);

    int countAllByEvent_IdAndStatus(Long eventId, TypeStateRequest status);


}
