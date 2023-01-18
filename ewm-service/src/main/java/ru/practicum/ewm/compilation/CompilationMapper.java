package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation mapToNewCompilation(NewCompilationDto dto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(new HashSet<>(events));
        compilation.setPinned(dto.getPinned());
        compilation.setTitle(dto.getTitle());

        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());

        List<EventShortDto> events = compilation.getEvents().stream()
                .map(EventMapper::mapToShortEventDto)
                .collect(Collectors.toList());
        dto.setEvents(events);

        return dto;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation, List<Event> events) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());

        List<EventShortDto> eventsShort = events.stream()
                .map(EventMapper::mapToShortEventDto)
                .collect(Collectors.toList());
        dto.setEvents(eventsShort);

        return dto;
    }
}
