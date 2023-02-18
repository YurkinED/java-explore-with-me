package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.users.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentDto {
    private Long id;
    @NotBlank
    @NotEmpty
    private String text;

    private EventShortDto event;
    private String authorName;


    private UserDto author;
    private LocalDateTime created;


}