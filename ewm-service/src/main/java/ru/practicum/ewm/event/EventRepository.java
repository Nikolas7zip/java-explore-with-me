package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query("SELECT ev, cat, user, loc FROM Event ev LEFT JOIN ev.category cat " +
            "LEFT JOIN ev.initiator user LEFT JOIN ev.location loc " +
            "WHERE ev.initiator.id=?1")
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("SELECT ev, cat, user, loc FROM Event ev LEFT JOIN ev.category cat " +
            "LEFT JOIN ev.initiator user LEFT JOIN ev.location loc " +
            "WHERE ev.id in ?1")
    List<Event> findAllByEventIds(List<Long> ids, Sort sort);

    @Query("SELECT ev, cat, user, loc FROM Event ev LEFT JOIN ev.category cat " +
            "LEFT JOIN ev.initiator user LEFT JOIN ev.location loc " +
            "WHERE ev.id=?1")
    Optional<Event> findEventById(Long id);

    @Modifying
    @Query("UPDATE Event ev SET ev.confirmedRequests = ?2 WHERE ev.id = ?1")
    int setConfirmedRequests(Long eventId, Integer confirmedRequests);

    long countByCategory_Id(Long categoryId);
}
