package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.pagination.EntityPagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto get(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        List<Event> events = new ArrayList<>();
        if (compilation.getEvents().size() != 0) {
            List<Long> eventIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            events = eventRepository.findAllByEventIds(eventIds, Sort.unsorted());
        }

        return CompilationMapper.mapToCompilationDto(compilation, events);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, EntityPagination pagination) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pagination.getPageable()).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pagination.getPageable());
        }

        List<CompilationDto> dtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<Long> ids = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            List<Event> events = new ArrayList<>();
            if (!ids.isEmpty()) {
                events = eventRepository.findAllByEventIds(ids, Sort.unsorted());
            }
            dtos.add(CompilationMapper.mapToCompilationDto(compilation, events));
        }

        return dtos;
    }

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (!newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllByEventIds(newCompilationDto.getEvents(), Sort.by("id").ascending());
        }
        if (events.size() != newCompilationDto.getEvents().size()) {
            throw new BadRequestException("Wrong event ids");
        }
        Compilation compilation = CompilationMapper.mapToNewCompilation(newCompilationDto, events);
        Compilation createdCompilation = compilationRepository.save(compilation);
        log.info("Created " + createdCompilation);

        return CompilationMapper.mapToCompilationDto(createdCompilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        compilationRepository.deleteById(compilation.getId());
        log.info("Deleted compilation with id=" + compId);
    }

    @Transactional
    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        Event eventToAdd = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        Set<Event> compilationEvents = compilation.getEvents();
        Optional<Event> checkedEvent = compilationEvents.stream()
                .filter(event -> event.getId().equals(eventId))
                .findAny();
        if (checkedEvent.isEmpty()) {
            compilationEvents.add(eventToAdd);
            compilation.setEvents(compilationEvents);
            compilationRepository.save(compilation);
        }

        log.info("Add event id=" + eventId + " to compilation id=" + compId);
    }

    @Transactional
    @Override
    public void deleteEvent(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        Set<Event> compilationEvents = compilation.getEvents();
        Set<Event> eventsAfterDelete = compilationEvents.stream()
                .filter(event -> !event.getId().equals(eventId))
                .collect(Collectors.toSet());
        if (eventsAfterDelete.size() != compilationEvents.size()) {
            compilation.setEvents(eventsAfterDelete);
            compilationRepository.save(compilation);
        } else {
            throw new BadRequestException("Event not found in compilation");
        }

        log.info("Delete event id=" + eventId + " from compilation id=" + compId);
    }

    @Transactional
    @Override
    public void pinOnMainPage(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Pin compilation with id=" + compId);
    }

    @Transactional
    @Override
    public void unpinFromMainPage(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Unpin compilation with id=" + compId);
    }
}
