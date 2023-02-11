package ru.practicum.support;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

@ControllerAdvice
public class ExceptionHandlerMain {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleException(ResponseStatusException e) {
        ApiError apiError = new ApiError(e.getMessage(), e.getReason(), e.getStatus(), Timestamp.from(Instant.now()), Arrays.asList(e.getStackTrace()));
        return ResponseEntity.status(e.getStatus()).body(apiError);
    }
}


