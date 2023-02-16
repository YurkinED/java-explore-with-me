package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ViewStats;
import ru.practicum.WebClient;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.TypeState;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.model.Request;
import ru.practicum.support.Validation;
import ru.practicum.users.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    private final WebClient webClient;

    @Override
    public List<EventFullDto> getEventsAdm(Long[] users, TypeState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllEvents(users, states, categories, rangeStart, rangeEnd, page);
        List<Long> list = new ArrayList<>();
        for (Event event1 : events) {
            list.add(event1.getId());
        }
        Collection<Request> requests = requestRepository.findByEventIdIn(list);
        addViews(events);
        return events.stream()
                .map(event -> eventMapper.convertEventToFullDto(event))
                .peek(x -> x.setConfirmedRequests(new Long(requests.stream().filter(y -> y.getEvent().getId().equals(x.getId())).count())))
                .collect(toList());
    }

    public Event patchEventsAdm(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findEventById(eventId);
        Validation.validationTime(event.getEventDate(), 1);
        Validation.validationTime(updateEventAdminRequest.getEventDate(), 1);
        eventMapper.updateEventAdminRequest(updateEventAdminRequest, event);
        event.setCategory(categoryService.getCategoryById(event.getCategory().getId()));
        if (!event.getState().equals(TypeState.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Event is unavailable. You can publish only event in PENDING");
        }
        switch (updateEventAdminRequest.getStateAction()) {
            case PUBLISH_EVENT:
                event.setState(TypeState.PUBLISHED);
                break;
            case REJECT_EVENT:
                event.setState(TypeState.CANCELED);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
        }
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Page<Event> getEventsByUserPriv(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        return eventRepository.findAllByInitiatorId(userId, page);
    }

    @Transactional(readOnly = true)
    public Event getEventByIdPriv(Long userId, Long eventId) {
        return eventRepository.findAllByIdAndInitiatorId(eventId, userId);
    }


    public Event postEventsByUserPriv(Long userId, NewEventDto newEventDto) {
        Validation.validationTime(newEventDto.getEventDate(), 2);
        Event event = eventMapper.convertNewEventDtoToEvent(newEventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userService.getUser(userId));
        event.setCategory(categoryService.getCategoryById(newEventDto.getCategory()));
        event.setState(TypeState.PENDING);
        return eventRepository.save(event);
    }

    public Event patchEventsByUserPriv(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = getEventByIdPriv(userId, eventId);
        Validation.validationTime(event.getEventDate(), 2);
        Validation.validationTime(updateEventUserRequest.getEventDate(), 2);
        eventMapper.updateEventUserRequest(updateEventUserRequest, event);
        event.setCategory(categoryService.getCategoryById(event.getCategory().getId()));
        if (event.getState().equals(TypeState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Event is unavailable. You can publish only event in PENDING or CANCELED");
        }
        switch (updateEventUserRequest.getStateAction()) {
            case SEND_TO_REVIEW:
                event.setState(TypeState.PENDING);
                break;
            case CANCEL_REVIEW:
                event.setState(TypeState.CANCELED);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
        }

        eventMapper.updateEventUserRequest(updateEventUserRequest, event);
        return eventRepository.save(event);
    }


    @Transactional(readOnly = true)
    public Page<Event> getEvents(String text, Long[] categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, Integer from, Integer size) {
        Pageable page;
        if (sort == null) {
            page = PageRequest.of((from / size), size);
        } else {
            switch (sort) {
                case "EVENT_DATE":
                    page = PageRequest.of((from / size), size, Sort.by(Sort.Direction.DESC, "eventDate"));
                    break;
                case "VIEWS":
                    page = PageRequest.of((from / size), size, Sort.by(Sort.Direction.DESC, "vievs"));
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
            }
        }
        if (onlyAvailable) {
            Collection<Event> event = eventRepository.searchEventsCollection(text, categories, paid, rangeStart, rangeEnd);
            List<Long> list = new ArrayList<>();
            for (Event event1 : event) {
                list.add(event1.getId());
            }
            Collection<Request> requests = requestRepository.findByEventIdIn(list);
            List<Event> eventResult = event.stream().filter(p -> p.getParticipantLimit() > requests.stream().filter(s -> s.getEvent().getId().equals(p.getId())).count()).collect(toList());
            return new PageImpl<>(eventResult, page, eventResult.size());
        } else {
            return eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, page);
        }
    }

    @Transactional(readOnly = true)
    public Event getPublishedEvent(Long eventId) {
        Optional<Event> event = eventRepository.findByIdAndState(eventId, TypeState.PUBLISHED);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event by this id not found");
        }
    }

    @Transactional(readOnly = true)
    public Event findEventById(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event by this id not found");
        }
    }

    @Transactional(readOnly = true)
    public Collection<Event> getEventList(List<Long> eventIds) {
        return eventRepository.findAllById(eventIds);
    }

    public void updateEvent(Event eventUpdate) {
        eventRepository.save(eventUpdate);
    }

    private void addViews(List<Event> events) {
        Map<Long, Event> eventMap = events.stream().collect(Collectors.toMap(Event::getId, event -> event));
        log.info(eventMap.toString());
        List<ViewStats> views = webClient.getViewsAll(eventMap.keySet());
        webClient.getViewsAll(eventMap.keySet());
        views.forEach(h -> eventMap.get(Long.parseLong(h.getUri().split("/")[1])).setViews(h.getHits()));

    }
}
