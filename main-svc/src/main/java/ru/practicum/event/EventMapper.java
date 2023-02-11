package ru.practicum.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {Location.class, Category.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    EventFullDto convertEventToFullDto(Event event);

    Collection<EventFullDto> convertCollEventToFullDto(Page<Event> event);

    EventShortDto convertEventToShortDto(Event event);

    Collection<EventShortDto> convertCollEventToShortDto(Page<Event> event);

    @Mapping(target = "category.id", source = "category")
    Event convertNewEventDtoToEvent(NewEventDto newEventDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "category.id", source = "category")
    void updateEventAdminRequest(UpdateEventAdminRequest updateEventAdminRequest, @MappingTarget Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "category.id", source = "category")
    void updateEventUserRequest(UpdateEventUserRequest updateEventUserRequest, @MappingTarget Event event);

}
