package ru.practicum.ewm.participation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.participation.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;

    private LocalDateTime created;

    private Long event;

    private Long requester;

    private ParticipationStatus status;
}
