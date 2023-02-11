package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserControllerAdmin {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers(@NotNull @RequestParam Long[] ids,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get list users ids={} from={}, size={}", ids, from, size);
        return new ResponseEntity<>(userMapper.convertCollUserToDto(userService.getUsers(ids, from, size)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody NewUserRequest newUser) {
        log.info("Post user {}", newUser);
        return new ResponseEntity<>(userMapper.convertUserToDto(userService.postUser(newUser)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@Positive @PathVariable Long userId) {
        log.info("Delete user with id={}", userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
