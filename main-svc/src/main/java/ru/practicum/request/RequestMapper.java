package ru.practicum.request;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.TypeStateRequest;
import ru.practicum.users.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {Event.class, User.class, TypeStateRequest.class})
public interface RequestMapper {

    @Mapping(source = "requester.id", target = "requester")
    @Mapping(source = "event.id", target = "event")
    ParticipationRequestDto convertRequestToDto(Request request);

    ParticipationRequestDto[] convertColRequestToDto(Collection<Request> request);


    Collection<ParticipationRequestDto> convertCollRequestToDto(Collection<Request> request);

}
