package ru.practicum.ewm.event;

import ru.practicum.ewm.event.comment.dto.CommentDto;
import ru.practicum.ewm.event.comment.dto.NewCommentDto;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.pagination.EntityPagination;

import java.util.List;

public interface EventService {

    EventFullDto getPublished(Long id);

    List<EventShortDto> getAllPublished(EntityPagination pagination, GetEventsPublicRequest request);

    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto getByInitiator(Long userId, Long eventId);

    EventFullDto updateByInitiator(Long userId, UserUpdateEventDto updateEventDto);

    List<EventShortDto> getAllByInitiator(Long userId, EntityPagination pagination);

    List<EventFullDto> getAllByAdmin(EntityPagination pagination, GetEventsAdminRequest request);

    EventFullDto cancelByInitiator(Long userId, Long eventId);

    EventFullDto publishByAdmin(Long eventId);

    EventFullDto cancelByAdmin(Long eventId);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventDto updateEventDto);

    void incrementViews(Long eventId);

    CommentDto addServiceCommentByAdmin(Long eventId, NewCommentDto newCommentDto);

    CommentDto addServiceCommentByInitiator(Long userId, Long eventId, NewCommentDto newCommentDto);

    List<CommentDto> getServiceComments(Long eventId);

    void deleteServiceComment(Long eventId, Long commentId);
}
