package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.GetStatsRequest;
import ru.practicum.ewm.dto.ViewStats;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@RestController
@Validated
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Value("${ewm.main-app}")
    private String ewmApp;

    @PostMapping("/hit")
    public void saveHit(@Valid @RequestBody EndpointHit hit) {
        if (!hit.getApp().equals(ewmApp)) {
            throw new IllegalArgumentException("Wrong ewm app");
        }
        statsService.saveHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "start") String start,
                                    @RequestParam(name = "end") String end,
                                    @RequestParam(name = "uris", required = false) Set<@NotBlank String> uris,
                                    @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        GetStatsRequest request = new GetStatsRequest(ewmApp, start, end, uris, unique);

        return statsService.getViewStats(request);
    }
}
