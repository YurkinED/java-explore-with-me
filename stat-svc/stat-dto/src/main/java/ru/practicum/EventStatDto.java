package ru.practicum;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
public class EventStatDto {
    private String app;
    private String uri;
    private String ip;
}
