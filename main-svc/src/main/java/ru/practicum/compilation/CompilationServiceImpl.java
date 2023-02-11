package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventService;
import ru.practicum.event.model.Event;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventService eventService;

    public Compilation postCompilation(NewCompilationDto newCompilationDto) {
        Collection<Event> events = eventService.getEventList(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.convertNewCompilationDtoToCompilation(newCompilationDto);
        compilation.setEvents(events);
        return compilationRepository.save(compilation);
    }

    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    public Compilation patchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Collection<Event> events = eventService.getEventList(updateCompilationRequest.getEvents());
        Compilation compilationUpd = getCompilation(compId);
        compilationMapper.convertUpdateCompilationRequestToCompilation(updateCompilationRequest, compilationUpd);
        compilationUpd.setEvents(events);
        eventsUpdate(events, compilationUpd);
        return compilationRepository.save(compilationUpd);
    }

    public Page<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        if (pinned == null) {
            return compilationRepository.findAll(page);
        } else {
            return compilationRepository.searchCompilations(pinned, page);
        }
    }

    public Compilation getCompilation(Long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isPresent()) {
            return compilation.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This compilation isn't found");
        }
    }

    private void eventsUpdate(Collection<Event> events, Compilation compilation) {
        if (compilation == null) {
            events.forEach(p -> p.setCompilationId(null));
        } else {
            events.forEach(p -> p.setCompilationId(compilation.getId()));
        }
    }
}
