package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.TypeState;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerAdmin {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Collection<EventFullDto>> getEventsAdm(@RequestParam(required = false) Long[] users,
                                                                 @RequestParam(required = false) TypeState[] states,
                                                                 @RequestParam(required = false) Long[] categories,
                                                                 @RequestParam(required = false) String rangeStart,
                                                                 @RequestParam(required = false) String rangeEnd,
                                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get list events from={}, size={}", from, size);
        return new ResponseEntity<>(eventMapper.convertCollEventToFullDto(eventService.getEventsAdm(users,
                states, categories, rangeStart, rangeEnd, from, size)), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEventsAdm(@NotNull @PathVariable Long eventId,
                                                       @NotNull @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Patch event id={}, eventUpd={}", eventId, updateEventAdminRequest);
        return new ResponseEntity<>(eventMapper.convertEventToFullDto(eventService.patchEventsAdm(eventId, updateEventAdminRequest)), HttpStatus.OK);
    }
}
