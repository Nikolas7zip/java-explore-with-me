package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class GetEventsPublicRequest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            this.rangeStart = LocalDateTime.parse(rangeStart, FORMATTER);
        }

        if (rangeEnd != null) {
            this.rangeEnd = LocalDateTime.parse(rangeEnd, FORMATTER);
        }
        this.onlyAvailable = onlyAvailable;
    }

}
