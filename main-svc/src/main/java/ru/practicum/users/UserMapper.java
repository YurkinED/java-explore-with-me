package ru.practicum.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto convertUserToDto(User user);

    Collection<UserDto> convertCollUserToDto(Page<User> user);


    UserShortDto convertUserToShortDto(User user);

    @Mapping(target = "id", ignore = true)
    User convertNewUserRequestToUser(NewUserRequest newUser);
}
