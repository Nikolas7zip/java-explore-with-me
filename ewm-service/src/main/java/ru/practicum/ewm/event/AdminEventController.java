package ru.practicum.ewm.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.comment.dto.CommentDto;
import ru.practicum.ewm.event.comment.dto.NewCommentDto;
import ru.practicum.ewm.event.dto.AdminUpdateEventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.GetEventsAdminRequest;
import ru.practicum.ewm.pagination.EntityPagination;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {
    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllEventsByAdmin(
            @RequestParam(name = "users", required = false) Set<@Positive Long> users,
            @RequestParam(name = "states", required = false) Set<EventState> states,
            @RequestParam(name = "categories", required = false) Set<@Positive Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        EntityPagination pagination = new EntityPagination(from, size, Sort.by("id").ascending());
        GetEventsAdminRequest request = new GetEventsAdminRequest(users, states, categories, rangeStart, rangeEnd);

        return eventService.getAllByAdmin(pagination, request);
    }

    @GetMapping("/pending")
    public List<EventFullDto> getPendingEvents(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        EntityPagination pagination = new EntityPagination(from, size, Sort.by("id").ascending());
        GetEventsAdminRequest request = new GetEventsAdminRequest(EventState.PENDING);

        return eventService.getAllByAdmin(pagination, request);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        return eventService.publishByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        return eventService.cancelByAdmin(eventId);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody AdminUpdateEventDto updateEventDto) {
        return eventService.updateByAdmin(eventId, updateEventDto);
    }

    @PostMapping("/{eventId}/comments")
    public CommentDto addServiceComment(@PathVariable Long eventId,
                                        @Valid @RequestBody NewCommentDto newCommentDto) {
        return eventService.addServiceCommentByAdmin(eventId, newCommentDto);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getServiceComments(@PathVariable Long eventId) {
        return eventService.getServiceComments(eventId);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    public void deleteServiceComment(@PathVariable Long eventId,
                                     @PathVariable Long commentId) {
        eventService.deleteServiceComment(eventId, commentId);
    }
}

