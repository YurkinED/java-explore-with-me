package ru.practicum.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/test")
@RequiredArgsConstructor
@Slf4j
@Validated
public class WhitePage {
    @GetMapping
    public String welcome() {
        return "login";
    }
}
