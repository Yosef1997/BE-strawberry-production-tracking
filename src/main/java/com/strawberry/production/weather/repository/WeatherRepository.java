package com.strawberry.production.weather.repository;

import com.strawberry.production.weather.entity.Weather;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query("SELECT w FROM Weather w " +
            "LEFT JOIN w.yield y " +
            "LEFT JOIN w.rejectRecords r " +
            "GROUP BY w.id " +
            "ORDER BY MAX(y.strawberryYield) DESC, (SUM(r.rejectDueToPest) + SUM(r.rejectDueToDisease)) ASC")
    List<Weather> findBestWeather(Pageable pageable);
}
