package ru.practicum.ewm;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewCount;
import ru.practicum.ewm.dto.ViewStats;

import java.util.ArrayList;
import java.util.List;

public class StatsMapper {

    public static View mapToNewView(EndpointHit hit) {
        View view = new View();
        view.setIp(hit.getIp());
        view.setTimestamp(hit.getTimestamp());
        view.setUri(hit.getUri());

        return view;
    }

    public static List<ViewStats> mapToViewStats(String app, List<ViewCount> counts) {
        List<ViewStats> stats = new ArrayList<>();
        for (ViewCount count : counts) {
            stats.add(new ViewStats(app, count.getTotal(), count.getUri()));
        }

        return stats;
    }
}
