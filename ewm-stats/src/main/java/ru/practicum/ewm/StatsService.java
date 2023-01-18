package ru.practicum.ewm;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.GetStatsRequest;
import ru.practicum.ewm.dto.ViewStats;

import java.util.List;

public interface StatsService {

    void saveHit(EndpointHit hit);

    List<ViewStats> getViewStats(GetStatsRequest request);
}
