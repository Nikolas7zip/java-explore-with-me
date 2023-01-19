package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.validation.EventDateValidator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class GetEventsAdminRequest {
    private Set<Long> users;
    private Set<EventState> states;
    private Set<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;

    public GetEventsAdminRequest(Set<Long> users,
                                 Set<EventState> states,
                                 Set<Long> categories,
                                 String rangeStart,
                                 String rangeEnd) {
        this.users = users;
        this.states = states;
        this.categories = categories;
        if (rangeStart != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, EventDateValidator.FORMATTER);
        }
        if (rangeEnd != null) {
            this.rangeEnd = LocalDateTime.parse(rangeEnd, EventDateValidator.FORMATTER);
        }
    }

    public GetEventsAdminRequest(EventState state) {
        this.states = new HashSet<>();
        this.states.add(state);
    }
}
