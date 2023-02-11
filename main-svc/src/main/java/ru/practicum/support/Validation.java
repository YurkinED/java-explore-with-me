package ru.practicum.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class Validation {

    public static void validationTime(LocalDateTime time, Integer hours) {
        if (time != null) {
            if (time.isBefore(LocalDateTime.now().plusHours(hours))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Event is unavailable. Need minimum " + hours + " hours before event");
            }
        }
    }

    public static LocalDateTime validationStartTime(String rangeStart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime starTime;
        if (rangeStart != null) {
            starTime = LocalDateTime.parse(rangeStart, formatter);
        } else {
            starTime = LocalDateTime.now();
        }
        return starTime;
    }

    public static LocalDateTime validationEndTime(String rangeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime endTime;
        if (rangeEnd != null) {
            endTime = LocalDateTime.parse(rangeEnd, formatter);
        } else {
            endTime = LocalDateTime.now().plusYears(100);
        }
        return endTime;
    }
}
