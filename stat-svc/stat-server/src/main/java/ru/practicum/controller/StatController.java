package ru.practicum.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EventStatDto;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatController {

    private final EventService eventService;

    @PostMapping("/hit")
    public ResponseEntity<EventStatDto> createEvent(@Valid @RequestBody EventStatDto eventStatDto) {
        log.info("controller:method userController -> createEvent");
        return ResponseEntity.status(201).body(eventService.createEvent(eventStatDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getEvents(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                           @RequestParam(required = false) List<String> uris,
                                                           @RequestParam(required = false, defaultValue = "false") Boolean uniquee

    ) {
        log.info("controller:method  -> getEvent");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(eventService.getEvents(start, end, uris, uniquee));
        System.out.println(json);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/test")
    public String welcome() {
        return "login";
    }


}
