package ru.practicum.ewm.user.prohibition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_prohibitions")
@Getter
@Setter
@ToString
public class Prohibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(name = "blocking_time", nullable = false)
    private Integer blockingTime;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;
}
