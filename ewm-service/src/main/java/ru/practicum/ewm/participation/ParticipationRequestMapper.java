package ru.practicum.ewm.participation;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.participation.dto.ParticipationRequestDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.participation.ParticipationStatus.*;

public class ParticipationRequestMapper {

    public static ParticipationRequest mapToNewParticipationRequest(Event event, Long requesterId) {
        ParticipationRequest participation = new ParticipationRequest();
        participation.setEventId(event.getId());
        participation.setRequesterId(requesterId);
        if (event.getRequestModeration().equals(Boolean.TRUE)) {
            participation.setStatus(PENDING);
        } else {
            participation.setStatus(CONFIRMED);
        }

        return participation;
    }

    public static ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest participation) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(participation.getId());
        dto.setCreated(participation.getCreated());
        dto.setEvent(participation.getEventId());
        dto.setRequester(participation.getRequesterId());
        dto.setStatus(participation.getStatus());

        return dto;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(List<ParticipationRequest> participations) {
        return participations.stream()
                .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
