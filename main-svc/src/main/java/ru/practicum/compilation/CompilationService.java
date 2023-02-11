package ru.practicum.compilation;

import org.springframework.data.domain.Page;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;


public interface CompilationService {

    Compilation postCompilation(NewCompilationDto Compilation);

    void deleteCompilation(Long compId);

    Compilation patchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    Page<Compilation> getCompilations(Boolean pinned, Integer from, Integer size);

    Compilation getCompilation(Long compId);


}
