package ru.practicum.ewm.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.GetEventsPublicRequest;
import ru.practicum.ewm.pagination.EntityPagination;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
@Validated
public class PublicEventController {
    private final EventService eventService;

    @Autowired
    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public EventFullDto getPublishedEvent(@PathVariable Long id) {
        EventFullDto dto = eventService.getPublished(id);
        dto.setViews(dto.getViews() + 1);       // TODO: views check
        return dto;
    }

    @GetMapping
    public List<EventShortDto> getAllPublishedEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) Set<@Positive Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort") EventSort sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        GetEventsPublicRequest request = new GetEventsPublicRequest(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable);
        Sort eventOrder = Sort.unsorted();
        if (sort == EventSort.VIEWS) {
            eventOrder = Sort.by("views").descending();
        } else if (sort == EventSort.EVENT_DATE) {
            eventOrder = Sort.by("eventDate").ascending();
        }
        EntityPagination pagination = new EntityPagination(from, size, eventOrder);

        return eventService.getAllPublished(pagination, request);
    }

}
