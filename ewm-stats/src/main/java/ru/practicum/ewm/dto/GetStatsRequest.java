package ru.practicum.ewm.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@ToString
public class GetStatsRequest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String app;

    private LocalDateTime start;

    private LocalDateTime end;

    private Set<String> uris;

    private Boolean unique;

    public GetStatsRequest(String app, String start, String end, Set<String> uris, Boolean unique) {
        this.app = app;
        this.start = LocalDateTime.parse(start, FORMATTER);
        this.end = LocalDateTime.parse(end, FORMATTER);
        this.uris = uris;
        this.unique = unique;
    }
}
