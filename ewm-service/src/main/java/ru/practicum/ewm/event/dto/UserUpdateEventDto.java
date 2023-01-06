package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.validation.EventDateConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEventDto {

    @NotNull
    private Long eventId;

    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @Positive
    private Long category;

    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    @EventDateConstraint
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Boolean paid;

    @NotNull
    @PositiveOrZero
    private Integer participantLimit;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
