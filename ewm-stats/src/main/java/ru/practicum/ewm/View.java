package ru.practicum.ewm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "views")
@Getter
@Setter
@ToString
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip", length = 39, nullable = false)
    private String ip;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "uri", length = 512, nullable = false)
    private String uri;
}
