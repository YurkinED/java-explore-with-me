package ru.practicum.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventMapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {EventMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {
    CompilationDto convertCompilationToDto(Compilation compilation);

    Collection<CompilationDto> convertCollCompilationToDto(Page<Compilation> compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation convertNewCompilationDtoToCompilation(NewCompilationDto newCompilationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    void convertUpdateCompilationRequestToCompilation(UpdateCompilationRequest updateCompilationRequest, @MappingTarget Compilation compilation);

}
