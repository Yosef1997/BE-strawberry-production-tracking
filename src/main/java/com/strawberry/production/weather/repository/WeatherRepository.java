package com.strawberry.production.weather.repository;

import com.strawberry.production.analysis.dto.AllDataDto;
import com.strawberry.production.analysis.dto.AnalysisResultDto;
import com.strawberry.production.weather.entity.Weather;
import org.springframework.data.domain.Page;
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

    @Query("SELECT new com.strawberry.production.analysis.dto.AllDataDto(" +
            "w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature, " +
            "COALESCE(SUM(y.strawberryYield), 0), " +
            "COALESCE(SUM(r.rejectDueToPest), 0), " +
            "COALESCE(SUM(r.rejectDueToDisease), 0)) " +
            "FROM Weather w " +
            "LEFT JOIN w.yield y ON y.deletedAt IS NULL " +
            "LEFT JOIN w.rejectRecords r ON r.deletedAt IS NULL " +
            "WHERE w.deletedAt IS NULL " +
            "GROUP BY w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature " +
            "ORDER BY w.id ASC")
    Page<AllDataDto> findAllWeatherDataWithYieldAndReject(Pageable pageable);

    @Query("SELECT new com.strawberry.production.analysis.dto.AllDataDto(" +
            "w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature, " +
            "COALESCE(SUM(y.strawberryYield), 0), " +
            "COALESCE(SUM(r.rejectDueToPest), 0), " +
            "COALESCE(SUM(r.rejectDueToDisease), 0)) " +
            "FROM Weather w " +
            "LEFT JOIN w.yield y ON y.deletedAt IS NULL " +
            "LEFT JOIN w.rejectRecords r ON r.deletedAt IS NULL " +
            "WHERE w.deletedAt IS NULL " +
            "GROUP BY w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature " +
            "ORDER BY COALESCE(SUM(y.strawberryYield), 0) ASC")
    Page<AllDataDto> findAllWeatherDataSortByYieldAsc(Pageable pageable);

    @Query("SELECT new com.strawberry.production.analysis.dto.AllDataDto(" +
            "w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature, " +
            "COALESCE(SUM(y.strawberryYield), 0), " +
            "COALESCE(SUM(r.rejectDueToPest), 0), " +
            "COALESCE(SUM(r.rejectDueToDisease), 0)) " +
            "FROM Weather w " +
            "LEFT JOIN w.yield y ON y.deletedAt IS NULL " +
            "LEFT JOIN w.rejectRecords r ON r.deletedAt IS NULL " +
            "WHERE w.deletedAt IS NULL " +
            "GROUP BY w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature " +
            "ORDER BY COALESCE(SUM(y.strawberryYield), 0) DESC")
    Page<AllDataDto> findAllWeatherDataSortByYieldDesc(Pageable pageable);

    @Query("SELECT new com.strawberry.production.analysis.dto.AllDataDto(" +
            "w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature, " +
            "COALESCE(SUM(y.strawberryYield), 0), " +
            "COALESCE(SUM(r.rejectDueToPest), 0), " +
            "COALESCE(SUM(r.rejectDueToDisease), 0)) " +
            "FROM Weather w " +
            "LEFT JOIN w.yield y ON y.deletedAt IS NULL " +
            "LEFT JOIN w.rejectRecords r ON r.deletedAt IS NULL " +
            "WHERE w.deletedAt IS NULL " +
            "GROUP BY w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature " +
            "ORDER BY (COALESCE(SUM(r.rejectDueToPest), 0) + COALESCE(SUM(r.rejectDueToDisease), 0)) ASC")
    Page<AllDataDto> findAllWeatherDataSortByTotalRejectAsc(Pageable pageable);

    @Query("SELECT new com.strawberry.production.analysis.dto.AllDataDto(" +
            "w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature, " +
            "COALESCE(SUM(y.strawberryYield), 0), " +
            "COALESCE(SUM(r.rejectDueToPest), 0), " +
            "COALESCE(SUM(r.rejectDueToDisease), 0)) " +
            "FROM Weather w " +
            "LEFT JOIN w.yield y ON y.deletedAt IS NULL " +
            "LEFT JOIN w.rejectRecords r ON r.deletedAt IS NULL " +
            "WHERE w.deletedAt IS NULL " +
            "GROUP BY w.id, w.weekNumber, w.humidity, w.rainFall, w.temperature " +
            "ORDER BY (COALESCE(SUM(r.rejectDueToPest), 0) + COALESCE(SUM(r.rejectDueToDisease), 0)) DESC")
    Page<AllDataDto> findAllWeatherDataSortByTotalRejectDesc(Pageable pageable);
}
