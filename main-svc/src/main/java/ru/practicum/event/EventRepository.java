package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.TypeState;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select e " +
            "from Event as e " +
            "WHERE e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3 " +
            "and e.eventDate between ?4 and ?5 ")
    Page<Event> findAllEvents(Long[] users, TypeState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    Event findAllByIdAndInitiatorId(Long eventId, Long userId);

    @Query(value = "select e " +
            "from Event as e " +
            "WHERE (LOWER(e.annotation) like LOWER(concat('%', ?1, '%')) " +
            "OR LOWER(e.description) like LOWER(concat('%', ?1, '%'))) " +
            "and e.category.id in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate between ?4 and ?5 " +
            "and (e.participantLimit - e.confirmedRequests) > 0 " +
            "and e.state = 'PUBLISHED'")
    Page<Event> searchEventsOnlyAvailable(String text, Long[] categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "select e " +
            "from Event as e " +
            "WHERE (LOWER(e.annotation) like LOWER(concat('%', ?1, '%')) " +
            "OR LOWER(e.description) like LOWER(concat('%', ?1, '%'))) " +
            "and e.category.id in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate between ?4 and ?5 " +
            "and e.state = 'PUBLISHED'")
    Page<Event> searchEvents(String text, Long[] categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    Optional<Event> findByIdAndState(Long eventId, TypeState state);


}
