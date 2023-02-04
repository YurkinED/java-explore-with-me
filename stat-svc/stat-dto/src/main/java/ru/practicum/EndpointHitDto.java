package ru.practicum;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;


@Jacksonized
@Data
public class EndpointHitDto {
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
