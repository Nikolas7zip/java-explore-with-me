package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.validation.EventDateValidator;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class GetEventsPublicRequest {
    private String text;
    private Set<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;

    public GetEventsPublicRequest(String text,
                                  Set<Long> categories,
                                  Boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  Boolean onlyAvailable) {
        this.text = text;
        this.categories = categories;
        this.paid = paid;
        if (rangeStart != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, EventDateValidator.FORMATTER);
        }

        if (rangeEnd != null) {
            this.rangeEnd = LocalDateTime.parse(rangeEnd, EventDateValidator.FORMATTER);
        }

        this.onlyAvailable = onlyAvailable;
    }

}
