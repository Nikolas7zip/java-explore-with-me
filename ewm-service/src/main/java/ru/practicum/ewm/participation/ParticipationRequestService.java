package ru.practicum.ewm.participation;

import ru.practicum.ewm.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestDto> getAllByRequester(Long requesterId);

    ParticipationRequestDto create(Long requesterId, Long eventId);

    ParticipationRequestDto cancelByRequester(Long requesterId, Long requestId);

    List<ParticipationRequestDto> getAllByEvent(Long initiatorId, Long eventId);

    ParticipationRequestDto confirmByInitiator(Long initiatorId, Long eventId, Long requestId);

    ParticipationRequestDto rejectByInitiator(Long initiatorId, Long eventId, Long requestId);
}
