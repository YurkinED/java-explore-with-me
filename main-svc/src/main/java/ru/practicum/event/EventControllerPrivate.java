package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.request.RequestMapper;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getEventsByUser(@NotNull @PathVariable Long userId,
                                                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get list events by userId={}, from={}, size={}", userId, from, size);
        return new ResponseEntity<>(eventMapper.convertCollEventToShortDto(eventService.getEventsByUserPriv(userId, from, size)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@Positive @PathVariable Long userId,
                                                     @Positive @PathVariable Long eventId) {
        log.info("Get full event by userId={} and id={}", userId, eventId);
        return new ResponseEntity<>(eventMapper.convertEventToFullDto(eventService.getEventByIdPriv(userId, eventId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> postEventsByUser(@Positive @PathVariable Long userId,
                                                         @Valid @NotNull @RequestBody NewEventDto newEventDto) {
        log.info("Post by userId={} new event={}", userId, newEventDto);
        return new ResponseEntity<>(eventMapper.convertEventToFullDto(eventService.postEventsByUserPriv(userId, newEventDto)), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEventsByUser(@Positive @PathVariable Long userId,
                                                          @Positive @PathVariable Long eventId,
                                                          @NotNull @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Patch event id={} by userId={}, update event={}", eventId, userId, updateEventUserRequest);
        return new ResponseEntity<>(eventMapper.convertEventToFullDto(eventService.patchEventsByUserPriv(userId, eventId, updateEventUserRequest)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<ParticipationRequestDto[]> getRequestEventByUser(@Positive @PathVariable Long userId,
                                                                           @Positive @PathVariable Long eventId) {
        log.info("Get request by userId={} and eventId={}", userId, eventId);
        return new ResponseEntity<>(requestMapper.convertColRequestToDto(requestService.getRequestEventByUser(userId, eventId)), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> patchRequestEventByUser(@Positive @PathVariable Long userId,
                                                                                  @Positive @PathVariable Long eventId,
                                                                                  @NotNull @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Patch requests userId={}, eventId={}, and eventRequestStatusUpdateRequest={}", userId, eventId, eventRequestStatusUpdateRequest);

        return new ResponseEntity<>(requestService.patchRequestEventByUser(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }

}
