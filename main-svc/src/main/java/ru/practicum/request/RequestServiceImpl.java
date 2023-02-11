package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.event.EventService;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.TypeState;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.TypeStateRequest;
import ru.practicum.users.UserService;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventService eventService;

    public Collection<Request> getRequestByUser(Long userId) {
        return requestRepository.findByRequesterId(userId);
    }

    public Request getRequestByIdAndUser(Long requestId, Long userId) {
        Optional<Request> request = requestRepository.findByIdAndRequesterId(requestId, userId);
        if (request.isPresent()) {
            return request.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request is not found");
        }
    }

    public Request postRequestByUser(Long userId, Long eventId) {
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This request is repeated");
        }
        Event event = eventService.findEvent(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Initiator of event can't post request");
        }
        if (!event.getState().equals(TypeState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is unavailable. Status of this event is't PUBLISHED");
        }
        if (event.getParticipantLimit() - event.getConfirmedRequests() == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is unavailable. the maximum number of visitors are reached");
        }
        TypeStateRequest state;
        if (event.isRequestModeration()) {
            state = TypeStateRequest.PENDING;
        } else {
            state = TypeStateRequest.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.updateEvent(event);
        }
        User user = userService.getUser(userId);
        Request request = Request.builder()
                .requester(user)
                .status(state)
                .created(LocalDateTime.now())
                .event(event)
                .build();
        return requestRepository.save(request);
    }

    public Request cancelRequestByUser(Long userId, Long requestId) {
        Request request = getRequestByIdAndUser(requestId, userId);
        request.setStatus(TypeStateRequest.CANCELED);
        Event event = request.getEvent();
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventService.updateEvent(event);
        return requestRepository.save(request);
    }

    public Collection<Request> getRequestEventByUser(Long userId, Long eventId) {
        return requestRepository.findByEventIdAndUser(userId, eventId);
    }

    public EventRequestStatusUpdateResult patchRequestEventByUser(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventService.findEvent(eventId);
        Collection<Request> requests = getRequestsById(eventRequestStatusUpdateRequest.getRequestIds());
        if ((!event.isRequestModeration()) || (event.getParticipantLimit() == 0)) {
            requests.forEach(p -> { p.setStatus(TypeStateRequest.CONFIRMED);
                                requestRepository.save(p);
                            }
                    );
            EventRequestStatusUpdateResult confirmedAll = new EventRequestStatusUpdateResult();
            confirmedAll.setConfirmedRequests(requestMapper.convertColRequestToDto(requests));
            return confirmedAll;
        }
        long countElementsPending = requests.stream()
                .filter(p -> p.getStatus().equals(TypeStateRequest.PENDING))
                .count();
        if (countElementsPending != requests.size()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update state is unavailable. Request hasn't state PENDING");
        }
        if (event.getParticipantLimit() - event.getConfirmedRequests() == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update state is unavailable. the maximum number of visitors are reached in event");
        }
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        for (Request request : requests) {
            switch (eventRequestStatusUpdateRequest.getStatus()) {
                case CONFIRMED:
                    if (event.getParticipantLimit() - event.getConfirmedRequests() > 0) {
                        request.setStatus(TypeStateRequest.CONFIRMED);
                        requestRepository.save(request);
                        confirmed.add(request);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        break;
                    }
                case REJECTED:
                    request.setStatus(TypeStateRequest.REJECTED);
                    requestRepository.save(request);
                    rejected.add(request);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
            }
        }
        eventService.updateEvent(event);
        EventRequestStatusUpdateResult requestResult = new EventRequestStatusUpdateResult();
        requestResult.setConfirmedRequests(requestMapper.convertColRequestToDto(confirmed));
        requestResult.setRejectedRequests(requestMapper.convertColRequestToDto(rejected));
        return requestResult;
    }


    private Collection<Request> getRequestsById(Long[] requestIds) {
        return requestRepository.findByIdIn(requestIds);
    }

}
