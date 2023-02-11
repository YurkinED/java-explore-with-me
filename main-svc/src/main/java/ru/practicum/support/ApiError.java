package ru.practicum.support;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private HttpStatus status;
    private Timestamp timestamp;
    private List<StackTraceElement> errors;

}
