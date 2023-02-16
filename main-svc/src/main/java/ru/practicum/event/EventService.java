package ru.practicum.event;

import org.springframework.data.domain.Page;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.TypeState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


public interface EventService {
    Page<Event> getEventsAdm(Long[] users, TypeState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                             Integer from, Integer size);

    Event patchEventsAdm(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Event getEventByIdPriv(Long userId, Long eventId);

    Page<Event> getEventsByUserPriv(Long userId, Integer from, Integer size);

    Event postEventsByUserPriv(Long userId, NewEventDto newEventDto);

    Event patchEventsByUserPriv(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    Page<Event> getEvents(String text, Long[] categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                          String sort, Integer from, Integer size);

    Event getPublishedEvent(Long eventId);

    Event findEventById(Long eventId);

    Collection<Event> getEventList(List<Long> eventIds);

    void updateEvent(Event eventUpdate);

}
