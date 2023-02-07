package ru.practicum;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;


@Jacksonized
@Data
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
