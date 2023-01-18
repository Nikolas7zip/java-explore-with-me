package ru.practicum.ewm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.ViewCount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsRepository extends JpaRepository<View, Long> {

    @Query("SELECT new ru.practicum.ewm.dto.ViewCount(v.uri, COUNT(v.uri)) " +
            "FROM View AS v WHERE v.timestamp >= ?1 AND v.timestamp <= ?2 " +
            "GROUP BY v.uri")
    List<ViewCount> countRangeViews(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.ViewCount(v.uri, COUNT(DISTINCT v.ip)) " +
            "FROM View AS v WHERE v.timestamp >= ?1 AND v.timestamp <= ?2 " +
            "GROUP BY v.uri")
    List<ViewCount> countRangeViewsForUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.ViewCount(v.uri, COUNT(v.uri)) " +
            "FROM View AS v WHERE v.timestamp >= ?1 AND v.timestamp <= ?2 AND v.uri IN ?3 " +
            "GROUP BY v.uri")
    List<ViewCount> countRangeViewsForUris(LocalDateTime start, LocalDateTime end, Set<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.ViewCount(v.uri, COUNT(DISTINCT v.ip)) " +
            "FROM View AS v WHERE v.timestamp >= ?1 AND v.timestamp <= ?2 AND v.uri IN ?3 " +
            "GROUP BY v.uri")
    List<ViewCount> countRangeViewsForUrisAndForUniqueIp(LocalDateTime start, LocalDateTime end, Set<String> uris);

}
