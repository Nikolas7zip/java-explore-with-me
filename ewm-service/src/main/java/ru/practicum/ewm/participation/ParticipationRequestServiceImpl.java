package ru.practicum.ewm.participation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.participation.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRepository;
    private final EventRepository eventRepository;

    public ParticipationRequestServiceImpl(ParticipationRequestRepository participationRepository,
                                           EventRepository eventRepository) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ParticipationRequestDto> getAllByRequester(Long requesterId) {
        Sort sort = Sort.by("id").descending();
        List<ParticipationRequest> participations = participationRepository.findAllByRequesterId(requesterId, sort);

        return ParticipationRequestMapper.mapToParticipationRequestDto(participations);
    }

    @Override
    public List<ParticipationRequestDto> getAllByEvent(Long initiatorId, Long eventId) {
        findInitiatorEvent(initiatorId, eventId);
        Sort sort = Sort.by("id").descending();
        List<ParticipationRequest> participations = participationRepository.findAllByEventId(eventId, sort);

        return ParticipationRequestMapper.mapToParticipationRequestDto(participations);
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long requesterId, Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        throwIfNotAllowedToParticipateInEvent(event, requesterId);
        ParticipationRequest participation = ParticipationRequestMapper.mapToNewParticipationRequest(event, requesterId);
        ParticipationRequest createdParticipation = participationRepository.save(participation);
        log.info("Created " + createdParticipation);

        if (createdParticipation.getStatus() == ParticipationStatus.CONFIRMED) {
            updateEventConfirmedRequests(event, 1);
        }

        return ParticipationRequestMapper.mapToParticipationRequestDto(createdParticipation);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelByRequester(Long requesterId, Long requestId) {
        ParticipationRequest participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ParticipationRequest.class, requestId));
        ParticipationStatus prevStatus = participation.getStatus();
        if (prevStatus == ParticipationStatus.REJECTED) {
            throw new BadRequestException("Can't cancel rejected participation request");
        }
        participation.setStatus(ParticipationStatus.CANCELED);
        ParticipationRequest updatedParticipation = participationRepository.save(participation);
        log.info("Cancel by requester " + updatedParticipation);
        if (prevStatus == ParticipationStatus.CONFIRMED) {
            Event event = eventRepository.findEventById(participation.getEventId())
                    .orElseThrow(() -> new EntityNotFoundException(Event.class, participation.getEventId()));
            updateEventConfirmedRequests(event, -1);
        }

        return ParticipationRequestMapper.mapToParticipationRequestDto(updatedParticipation);
    }

    @Transactional
    @Override
    public ParticipationRequestDto confirmByInitiator(Long initiatorId, Long eventId, Long requestId) {
        Event event = findInitiatorEvent(initiatorId, eventId);
        ParticipationRequest participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ParticipationRequest.class, requestId));
        if (!participation.getEventId().equals(eventId)) {
            throw new BadRequestException("Event and participation request do not match");
        }

        if (participation.getStatus() != ParticipationStatus.PENDING) {
            throw new BadRequestException("No confirmation required");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new BadRequestException("Participation limit has been reached");
        }

        participation.setStatus(ParticipationStatus.CONFIRMED);
        ParticipationRequest updatedParticipation = participationRepository.save(participation);
        log.info("Confirm by initiator " + updatedParticipation);
        updateEventConfirmedRequests(event, 1);
        if (event.getParticipantLimit().equals(event.getConfirmedRequests() + 1)) {
            participationRepository.setRejectedToPendingParticipationRequests(eventId);
        }

        return ParticipationRequestMapper.mapToParticipationRequestDto(updatedParticipation);
    }

    @Transactional
    @Override
    public ParticipationRequestDto rejectByInitiator(Long initiatorId, Long eventId, Long requestId) {
        findInitiatorEvent(initiatorId, eventId);
        ParticipationRequest participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ParticipationRequest.class, requestId));
        if (!participation.getEventId().equals(eventId)) {
            throw new BadRequestException("Event and participation request do not match");
        }

        if (participation.getStatus() != ParticipationStatus.PENDING) {
            throw new BadRequestException("Only pending participation request can be rejected");
        }
        participation.setStatus(ParticipationStatus.REJECTED);
        ParticipationRequest updatedParticipation = participationRepository.save(participation);
        log.info("Rejected by initiator " + updatedParticipation);

        return ParticipationRequestMapper.mapToParticipationRequestDto(updatedParticipation);
    }

    private void throwIfNotAllowedToParticipateInEvent(Event event, Long requesterId) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new BadRequestException("Not allowed to participate in unpublished event");
        } else if (event.getInitiator().getId().equals(requesterId)) {
            throw new BadRequestException("Initiator can't make participation request in his event");
        } else if (!participationRepository.findByEventIdAndRequesterId(event.getId(), requesterId).isEmpty()) {
            throw new BadRequestException("Not allowed to make repeated participation request");
        } else if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new BadRequestException("Participation limit has been reached");
        }
    }

    private void updateEventConfirmedRequests(Event event, int diff) {
        Integer newConfirmedRequest = event.getConfirmedRequests() + diff;
        int result = eventRepository.setConfirmedRequests(event.getId(), newConfirmedRequest);
        log.info("Update result confirmed request " + result);
    }

    private Event findInitiatorEvent(Long initiatorId, Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        if (!event.getInitiator().getId().equals(initiatorId)) {
            throw new BadRequestException("User is not initiator of event with id=" + eventId);
        }

        return event;
    }
}
