package ru.practicum.ewm.event;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.EventState.*;

public class EventMapper {

    public static Event mapToNewEvent(NewEventDto dto, User initiator, Category category) {
        Event event = new Event();
        Location location = new Location();
        location.setLatitude(dto.getLocation().getLat());
        location.setLongitude(dto.getLocation().getLon());

        event.setAnnotation(dto.getAnnotation());
        event.setCategory(category);
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(location);
        event.setInitiator(initiator);
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setState(PENDING);
        event.setTitle(dto.getTitle());

        return event;
    }

    public static EventFullDto mapToFullEventDto(Event event) {
        EventFullDto dto = new EventFullDto();
        UserShortDto userShortDto = new UserShortDto(
                event.getInitiator().getId(),
                event.getInitiator().getName()
        );
        CategoryDto categoryDto = new CategoryDto(
                event.getCategory().getId(),
                event.getCategory().getName()
        );
        LocationDto locationDto = new LocationDto(
                event.getLocation().getLatitude(),
                event.getLocation().getLongitude()
        );

        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(categoryDto);
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(userShortDto);
        dto.setLocation(locationDto);
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }

    public static List<EventFullDto> mapToFullEventDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToFullEventDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto mapToShortEventDto(Event event) {
        EventShortDto dto = new EventShortDto();
        CategoryDto categoryDto = new CategoryDto(
                event.getCategory().getId(),
                event.getCategory().getName()
        );
        UserShortDto userShortDto = new UserShortDto(
                event.getInitiator().getId(),
                event.getInitiator().getName()
        );

        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(categoryDto);
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(userShortDto);
        dto.setPaid(event.getPaid());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());

        return dto;
    }

    public static List<EventShortDto> mapToShortEventDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToShortEventDto)
                .collect(Collectors.toList());
    }

    public static void updateEventFromUserDto(Event event, UserUpdateEventDto dto, Category newCategory) {
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(newCategory);
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setTitle(dto.getTitle());

        if (event.getState() == CANCELED) {
            event.setState(PENDING);
        }
    }

    public static void updateEventFromAdminDto(Event event, AdminUpdateEventDto dto, Category newCategory) {
        if (newCategory != null) {
            event.setCategory(newCategory);
        }

        LocationDto locationDto = dto.getLocation();
        if (locationDto != null) {
            event.getLocation().setLatitude(locationDto.getLat());
            event.getLocation().setLongitude(locationDto.getLon());
        }

        event.setAnnotation((dto.getAnnotation() != null) ? dto.getAnnotation() : event.getAnnotation());
        event.setDescription((dto.getDescription() != null) ? dto.getDescription() : event.getDescription());
        event.setEventDate((dto.getEventDate() != null) ? dto.getEventDate() : event.getEventDate());
        event.setPaid((dto.getPaid() != null) ? dto.getPaid() : event.getPaid());
        event.setParticipantLimit((dto.getParticipantLimit() != null) ? dto.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration((dto.getRequestModeration() != null) ? dto.getRequestModeration() : event.getRequestModeration());
        event.setTitle((dto.getTitle() != null) ? dto.getTitle() : event.getTitle());
    }
}
