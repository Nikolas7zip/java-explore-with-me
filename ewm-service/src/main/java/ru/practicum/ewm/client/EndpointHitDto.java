package ru.practicum.ewm.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EndpointHitDto {
    private String app;
    private String ip;
    private String uri;
    private String timestamp;
}
