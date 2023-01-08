package ru.practicum.ewm.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.GetEventsPublicRequest;
import ru.practicum.ewm.pagination.EntityPagination;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
@Validated
public class PublicEventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @Autowired
    public PublicEventController(EventService eventService,
                                 StatsClient statsClient) {
        this.eventService = eventService;
        this.statsClient = statsClient;
    }

    @GetMapping("/{id}")
    public EventFullDto getPublishedEvent(@PathVariable Long id, HttpServletRequest request) {
        EventFullDto dto = eventService.getPublished(id);
        dto.setViews(dto.getViews() + 1);

        statsClient.saveEndpointHit(request.getRemoteAddr(), request.getRequestURI());
        eventService.incrementViews(id);

        return dto;
    }

    @GetMapping
    public List<EventShortDto> getAllPublishedEvents(
            HttpServletRequest request,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) Set<@Positive Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") EventSort sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        GetEventsPublicRequest getEventsRequest = new GetEventsPublicRequest(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable);
        Sort eventOrder = Sort.unsorted();
        if (sort == EventSort.VIEWS) {
            eventOrder = Sort.by("views").descending();
        } else if (sort == EventSort.EVENT_DATE) {
            eventOrder = Sort.by("eventDate").ascending();
        }
        EntityPagination pagination = new EntityPagination(from, size, eventOrder);
        List<EventShortDto> publishedEvents = eventService.getAllPublished(pagination, getEventsRequest);

        statsClient.saveEndpointHit(request.getRemoteAddr(), request.getRequestURI());

        return publishedEvents;
    }

}
