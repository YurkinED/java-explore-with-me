package ru.practicum;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Jacksonized
@Data
public class EndpointHitReturnDto {
    Integer id;
    String app;
    String uri;
    LocalDateTime timestamp;
}
