package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.WebClient;
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

    private final WebClient webClient;

    @Value("${app-name}")
    private String appName;

    @Value("${statistic-server.uri}")
    private String serverUrl;

    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getEvents(@RequestParam(defaultValue = "") String text,
                                                               @RequestParam(required = false) Long[] categories,
                                                               @RequestParam(required = false) boolean paid,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                               @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                               @RequestParam(required = false) String sort,
                                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                               @Positive @RequestParam(defaultValue = "10") Integer size,
                                                               HttpServletRequest request) {
        log.info("Get list events from={}, size={}", from, size);
        log.info("serverUrl={}", serverUrl + "/hit");
        webClient.addToStatistic(appName, request);
        return new ResponseEntity<>(eventMapper.convertCollEventToShortDto(eventService.getEvents(text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@NotNull @PathVariable Long eventId, HttpServletRequest request) {
        log.info("Get full event by id={}", eventId);
        webClient.addToStatistic(appName, request);
        return new ResponseEntity<>(eventMapper.convertEventToFullDto(eventService.getEvent(eventId)), HttpStatus.OK);
    }


}
