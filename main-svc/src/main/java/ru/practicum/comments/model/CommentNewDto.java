package ru.practicum.comments.model;


import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentNewDto {
    private Long id;
    private String authorName;
    @Size(min = 1, max = 2000)
    @NotBlank
    String text;
    @NotNull
    private Long eventId;
    private LocalDateTime created;

}