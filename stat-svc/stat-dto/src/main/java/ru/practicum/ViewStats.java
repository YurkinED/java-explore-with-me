package ru.practicum;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@Builder
@AllArgsConstructor
public class ViewStats {
    private String uri;
    private String app;
    private Long hits;
}