package ru.practicum;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Jacksonized
@Data
public class EndpointHitReturnDto {
    private Integer id;
    private String app;
    private String uri;
    private LocalDateTime timestamp;
}
