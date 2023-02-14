package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.CategoryService;
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

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    // private final RequestService requestService;

    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    public Page<Event> getEventsAdm(Long[] users, TypeState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        return eventRepository.findAllEvents(users, states, categories, rangeStart, rangeEnd, page);
    }

    public Event patchEventsAdm(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findEvent(eventId);
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

    public Page<Event> getEventsByUserPriv(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        return eventRepository.findAllByInitiatorId(userId, page);
    }

    public Event getEventByIdPriv(Long userId, Long eventId) {
        return eventRepository.findAllByIdAndInitiatorId(eventId, userId);
    }


    public Event postEventsByUserPriv(Long userId, NewEventDto newEventDto) {
        Validation.validationTime(newEventDto.getEventDate(), 2);
        Event event = eventMapper.convertNewEventDtoToEvent(newEventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userService.getUser(userId));
        // event.setConfirmedRequests(0L);
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
            List<Event> list = new ArrayList<>();
            for (Event event1 : event) {
                list.add(event1);
            }
            Collection<Request> requests = requestRepository.findByEventIdIn(list);
            List<Event> eventResult = event.stream().filter(p -> p.getParticipantLimit() > requests.stream().filter(s -> s.getEvent().getId().equals(p.getId())).count()).collect(toList());
            return new PageImpl<>(eventResult, page, eventResult.size());
        } else {
            return eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, page);
        }
    }

    /*
    public Page<Event> getEvents(String text, Long[] categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                                 String sort, Integer from, Integer size) {
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


            //requestRepository
           //return eventRepository.searchEventsOnlyAvailable(text, categories, paid, rangeStart, rangeEnd, page);
            Page<Event> event=eventRepository.searchEventsOnlyAvailable(text, categories, paid, rangeStart, rangeEnd, page);
        //    return event.stream().filter(p -> p.getParticipantLimit() > requestRepository.findByEventIdIn(p.getId()).stream().count());
          //  Map<Long, Long> requests = new HashMap<>();
          //for (Request request : requestService.getRequestByEventId(event.stream().collect(groupingBy(Event::getId, toList())).keySet())) {
          //      requests.merge(request.getEvent().getId(), 1L, Long::sum);
          //  }
        //   Page<Event> event2 = (Page<Event>) event.stream().filter(p -> p.getParticipantLimit() > requests.get(p.getId()));
       //     return event2;
        } else {
            return eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, page);
        }
    }
*/
    public Event getEvent(Long eventId) {
        Optional<Event> event = eventRepository.findByIdAndState(eventId, TypeState.PUBLISHED);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event by this id not found");
        }
    }

    public Event findEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event by this id not found");
        }
    }

    public Collection<Event> getEventList(List<Long> eventIds) {
        return eventRepository.findAllById(eventIds);
    }

    public void updateEvent(Event eventUpdate) {
        eventRepository.save(eventUpdate);
    }
}
