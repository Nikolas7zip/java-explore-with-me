package ru.practicum.ewm.event.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_comments")
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", updatable = false)
    private User author;

    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "type", length = 24, nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentType type;
}
