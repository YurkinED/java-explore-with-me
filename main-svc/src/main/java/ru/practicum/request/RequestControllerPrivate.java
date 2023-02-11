package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestControllerPrivate {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping
    public ResponseEntity<Collection<ParticipationRequestDto>> getRequestByUser(@Positive @PathVariable Long userId) {
        log.info("Get list request by userId={}", userId);
        return new ResponseEntity<>(requestMapper.convertCollRequestToDto(requestService.getRequestByUser(userId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> postRequestByUser(@Positive @PathVariable Long userId,
                                                                     @Positive @RequestParam Long eventId) {
        log.info("Post request by userId={} on eventId={}", userId, eventId);
        return new ResponseEntity<>(requestMapper.convertRequestToDto(requestService.postRequestByUser(userId, eventId)), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequestByUser(@Positive @PathVariable Long userId,
                                                                       @Positive @PathVariable Long requestId) {

        log.info("Patch by userId={} on requestId={}", userId, requestId);
        return new ResponseEntity<>(requestMapper.convertRequestToDto(requestService.cancelRequestByUser(userId, requestId)), HttpStatus.OK);
    }

}
