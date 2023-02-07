package ru.practicum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ViewStats {
    private final String uri;
    private final String app;
    private final Long hits;
}