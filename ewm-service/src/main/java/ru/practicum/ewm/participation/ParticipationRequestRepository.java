package ru.practicum.ewm.participation;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId, Sort sort);

    List<ParticipationRequest> findAllByEventId(Long eventId, Sort sort);


    @Modifying
    @Query("UPDATE ParticipationRequest part " +
            "SET part.status = ru.practicum.ewm.participation.ParticipationStatus.REJECTED " +
            "WHERE part.eventId = ?1 AND part.status = ru.practicum.ewm.participation.ParticipationStatus.PENDING")
    int setRejectedToPendingParticipationRequests(Long eventId);
}
