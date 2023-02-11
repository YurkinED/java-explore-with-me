package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<User> getUsers(Long[] ids, Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        return userRepository.findAllByIds(ids, page);
    }

    public User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User by this id not found");
        }
    }

    public User postUser(NewUserRequest newUser) {
        try {
            return userRepository.save(userMapper.convertNewUserRequestToUser(newUser));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user is duplicated");
        }
    }

    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user isn't empty");
        }
    }
}
