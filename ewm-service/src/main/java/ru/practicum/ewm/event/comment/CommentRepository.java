package ru.practicum.ewm.event.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT com, user FROM Comment com LEFT JOIN com.author user " +
            "WHERE com.eventId = ?1 AND com.type = ru.practicum.ewm.event.comment.CommentType.SERVICE")
    List<Comment> getServiceComments(Long eventId);
}
