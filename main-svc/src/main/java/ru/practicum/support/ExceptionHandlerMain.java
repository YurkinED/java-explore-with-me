package ru.practicum.support;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerMain {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleException(ResponseStatusException e) {
        log.error(e.getMessage());
        ApiError apiError = new ApiError(e.getMessage(), e.getReason(), e.getStatus(), Timestamp.from(Instant.now()), Arrays.asList(e.getStackTrace()));
        return ResponseEntity.status(e.getStatus()).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleThrowable(final Throwable e) {
        log.error("handleThrowable: {}", e.getMessage());
        e.printStackTrace();
        log.error("handleThrowable: {}", e.getStackTrace());
        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var apiError = ApiError.builder()
                .status(status)
                .message(e.getMessage())
                .reason("Unexpected error")
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException: {}", e.getMessage());
        final var field = e.getBindingResult().getFieldErrors().stream()
                .map(err -> "Field: " + err.getField() + ". Error: " + err.getDefaultMessage() + ". Value: " +
                        err.getRejectedValue())
                .collect(Collectors.toList());
        final String errors = field.toString().substring(1, field.toString().length() - 1);
        log.warn(errors);
        final var apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(errors)
                .reason("Incorrectly made request.")
                .build();
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> methodArgumentNotValidException2(final MissingServletRequestParameterException e) {
        log.error("methodArgumentNotValidException2: {}", e.getMessage());
        final var field = e.getMessage();
        final String errors = field.substring(1, field.toString().length() - 1);
        log.warn(errors);
        final var apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(errors)
                .reason("Incorrectly made request.")
                .build();
        return ResponseEntity.status(400).body(apiError);
    }
}


