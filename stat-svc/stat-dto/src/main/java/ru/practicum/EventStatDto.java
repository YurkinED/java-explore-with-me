package ru.practicum;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
@Builder
public class EventStatDto {
    private String app;
    private String uri;
    private String ip;
}
