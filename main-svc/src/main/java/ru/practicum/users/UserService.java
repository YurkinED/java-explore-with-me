package ru.practicum.users;

import org.springframework.data.domain.Page;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.model.User;


public interface UserService {
    Page<User> getUsers(Long[] ids, Integer from, Integer size);

    User getUser(Long userId);


    User postUser(NewUserRequest newUser);

    void deleteUser(Long userId);
}
