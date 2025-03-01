package com.strawberry.production.yield.repository;

import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.yield.entity.Yield;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YieldRepository extends JpaRepository<Yield, Long> {
    List<Yield> findByWeekNumber(Weather weekNumber);
}
