package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EventStatDto;
import ru.practicum.dao.EventHits;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {

    private final EventService eventService;

    @PostMapping("/hit")
    public ResponseEntity<EventStatDto> createEvent(@Valid @RequestBody EventStatDto eventStatDto) {
        log.info("controller:method userController -> createUser");
        return ResponseEntity.ok(eventService.createEvent(eventStatDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<Collection<EventHits>> getEvents(@RequestParam String start,
                                                           @RequestParam String end,
                                                           @RequestParam List<String> uris

    ) {
        log.info("controller:method  -> createEvent");
        return ResponseEntity.ok(eventService.getEvents(start, end, uris));
    }


}
