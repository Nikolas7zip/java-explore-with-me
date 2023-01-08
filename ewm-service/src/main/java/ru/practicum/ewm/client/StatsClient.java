package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.event.dto.GetEventsPublicRequest;

import java.time.LocalDateTime;

@Service
public class StatsClient extends BaseClient {
    @Value("${ewm.main-app}")
    private String ewmApp;

    @Autowired
    public StatsClient(@Value("${ewm.stats-server}") String statsUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void saveEndpointHit(String ipAddress, String uri) {
        String timestamp = LocalDateTime.now().format(GetEventsPublicRequest.FORMATTER);
        EndpointHitDto dto = new EndpointHitDto(ewmApp, ipAddress, uri, timestamp);
        ResponseEntity<Object> response = post("/hit", dto);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Can't save stats endpoint " + uri);
        }
    }

}
