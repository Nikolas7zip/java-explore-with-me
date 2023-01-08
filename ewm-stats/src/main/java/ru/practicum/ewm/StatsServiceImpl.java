package ru.practicum.ewm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.GetStatsRequest;
import ru.practicum.ewm.dto.ViewCount;
import ru.practicum.ewm.dto.ViewStats;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }


    @Transactional
    @Override
    public void saveHit(EndpointHit hit) {
        View createdView = statsRepository.save(StatsMapper.mapToNewView(hit));
        log.info("Add view " + createdView);
    }

    @Override
    public List<ViewStats> getViewStats(GetStatsRequest request) {
        List<ViewCount> counts;
        if (request.getUnique().equals(Boolean.TRUE) && request.getUris() != null) {
            counts = statsRepository.countRangeViewsForUrisAndForUniqueIp(request.getStart(), request.getEnd(),
                    request.getUris());
        } else if (request.getUnique().equals(Boolean.TRUE)) {
            counts = statsRepository.countRangeViewsForUniqueIp(request.getStart(), request.getEnd());
        } else if (request.getUris() != null) {
            counts = statsRepository.countRangeViewsForUris(request.getStart(), request.getEnd(), request.getUris());
        } else {
            counts = statsRepository.countRangeViews(request.getStart(), request.getEnd());
        }


        return StatsMapper.mapToViewStats(request.getApp(), counts);
    }
}
