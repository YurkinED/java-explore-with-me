package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EventStatDto;
import ru.practicum.dao.EventHits;
import ru.practicum.dao.EventRepository;
import ru.practicum.mappers.EventStatMapper;
import ru.practicum.model.EventStat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventStatDto createEvent(EventStatDto eventStatDto) {
        EventStat eventStat = EventStatMapper.INSTANCE.eventStatDtoToEventStat(eventStatDto);
        eventStat.setCreated(LocalDateTime.now());
        return EventStatMapper.INSTANCE.eventStatToEventStatDto(eventRepository
                .save(eventStat));
    }

    public Collection<EventHits> getEvents(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean uniquee) {
        if (uniquee) {
            return eventRepository.findAllStatsWithFilterDistinct(uris, start, end);
        } else {
            return eventRepository.findAllStatsWithFilter(uris, start, end);
        }
    }

}
