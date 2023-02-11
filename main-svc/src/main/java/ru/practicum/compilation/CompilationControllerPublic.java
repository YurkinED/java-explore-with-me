package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerPublic {
    private final CompilationServiceImpl compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping
    public ResponseEntity<Collection<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get list compilations pinned={} from={}, size={}", pinned, from, size);
        return new ResponseEntity<>(compilationMapper.convertCollCompilationToDto(compilationService.getCompilations(pinned, from, size)), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@Positive @PathVariable Long compId) {
        log.info("Get compilation id={}", compId);
        return new ResponseEntity<>(compilationMapper.convertCompilationToDto(compilationService.getCompilation(compId)), HttpStatus.OK);
    }

}
