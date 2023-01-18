package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.pagination.EntityPagination;

import java.util.List;

public interface CompilationService {

    CompilationDto get(Long compId);

    List<CompilationDto> getAll(Boolean pinned, EntityPagination pagination);

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    void addEvent(Long compId, Long eventId);

    void deleteEvent(Long compId, Long eventId);

    void pinOnMainPage(Long compId);

    void unpinFromMainPage(Long compId);
}
