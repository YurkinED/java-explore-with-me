package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.EventStatDto;
import ru.practicum.dao.EventHits;
import ru.practicum.dao.EventRepository;
import ru.practicum.mappers.EventStatMapper;
import ru.practicum.model.EventStat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventStatDto createEvent(EventStatDto eventStatDto) {
        EventStat eventStat = EventStatMapper.INSTANCE.eventStatDtoToEventStat(eventStatDto);
        eventStat.setCreated(LocalDateTime.now());
        return EventStatMapper.INSTANCE.eventStatToEventStatDto(eventRepository
                .save(eventStat));
    }

    public Collection<EventHits> getEvents(String start, String end, List<String> uris) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        return eventRepository.findAllStatsWithFilter(uris, startDate, endDate);
    }

}
