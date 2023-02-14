package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message= "annotation could not be blank")
    @Size(max = 500)
    private String annotation;
    @NotNull(message= "category could not be blank")
    private Long category;
    @NotBlank(message= "description could not be blank")
    @Size(max = 1000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
   @NotNull(message= "location could not be blank")
    private Location location;
    private boolean paid;
    @PositiveOrZero
    @NotNull(message= "participantLimit could not be blank")
    private Long participantLimit = 0L;
    @NotNull(message= "requestModeration could not be blank")
    private Boolean requestModeration = true;
    @NotBlank(message= "title could not be blank")
    @Size(max = 200)
    private String title;
}
