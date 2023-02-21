package ru.practicum.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerMain {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e) {
        log.error("handleUserNotFoundException={}",e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .reason("The required object was not found.")
                        .message(e.getMessage())
                        .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                        .build());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiError> handleCommentNotFoundException(CommentNotFoundException e) {
        log.error("handleCommentNotFoundException={}",e.getMessage());
        return ResponseEntity
                .status(HttpStatus.valueOf(204))
                .body(ApiError.builder()
                        .status(HttpStatus.valueOf(204))
                        .reason("Comment not found")
                        .message(e.getMessage())
                        .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("httpMessageNotReadableException={}",e.getMessage());
        ApiError apiError = new ApiError(e.getMessage(), e.getMessage(), HttpStatus.CONFLICT, Timestamp.from(Instant.now()), Arrays.asList(e.getStackTrace()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> responseStatusException(ResponseStatusException e) {
        log.error("responseStatusException={}",e.getMessage());
        ApiError apiError = new ApiError(e.getMessage(), e.getReason(), e.getStatus(), Timestamp.from(Instant.now()), Arrays.asList(e.getStackTrace()));
        return ResponseEntity.status(e.getStatus()).body(apiError);
    }


    @ExceptionHandler
    public ResponseEntity<ApiError> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException: {}", e.getMessage());
        final String field = e.getBindingResult().getFieldErrors().stream()
                .map(err -> "Field: " + err.getField() + ". Error: " + err.getDefaultMessage() + ". Value: " +
                        err.getRejectedValue())
                .collect(Collectors.toList()).toString();
        final String errors = field.substring(1, field.length() - 1);
        log.warn(errors);
        final ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(errors)
                .reason("Incorrectly made request.")
                .build();
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> missingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error("methodArgumentNotValidException2: {}", e.getMessage());
        final String field = e.getMessage();
        final String errors = field.substring(1, field.length() - 1);
        log.warn(errors);


        final ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(errors)
                .reason("Incorrectly made request.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleThrowable(final Throwable e) {
        log.error("handleThrowable: {}", e.getMessage());
        e.printStackTrace();
        log.error("handleThrowable: {}", e.getStackTrace());
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError apiError = ApiError.builder()
                .status(status)
                .message(e.getMessage())
                .reason("Unexpected error")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError);
    }


}


