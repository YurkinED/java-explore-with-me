package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EventsClient;
import ru.practicum.EndpointHitDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPublic {
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventsClient eventsClient;

    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getEvents(@RequestParam(defaultValue = "") String text,
                                                               @RequestParam(required = false) Long[] categories,
                                                               @RequestParam(required = false) boolean paid,
                                                               @RequestParam(required = false) String rangeStart,
                                                               @RequestParam(required = false) String rangeEnd,
                                                               @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                               @RequestParam(required = false) String sort,
                                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                               @Positive @RequestParam(defaultValue = "10") Integer size,
                                                               HttpServletRequest request) {
        log.info("Get list events from={}, size={}", from, size);
        eventsClient.postStatistic("", new EndpointHitDto("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(eventMapper.convertCollEventToShortDto(eventService.getEvents(text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@NotNull @PathVariable Long eventId, HttpServletRequest request) {
        log.info("Get full event by id={}", eventId);
        eventsClient.postStatistic("", new EndpointHitDto("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(eventMapper.convertEventToFullDto(eventService.getEvent(eventId)), HttpStatus.OK);
    }


}
