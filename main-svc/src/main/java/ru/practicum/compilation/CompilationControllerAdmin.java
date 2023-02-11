package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerAdmin {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping
    public ResponseEntity<CompilationDto> postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post compilation {}", newCompilationDto);
        return new ResponseEntity<>(compilationMapper.convertCompilationToDto(compilationService.postCompilation(newCompilationDto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<HttpStatus> deleteCompilation(@Positive @PathVariable Long compId) {
        log.info("Delete compilation by id={}", compId);
        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patchCompilation(@Positive @PathVariable Long compId,
                                                           @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Patch compilation by id={}", compId);
        CompilationDto compilationDto = compilationMapper.convertCompilationToDto(compilationService.patchCompilation(compId, updateCompilationRequest));
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }


}

