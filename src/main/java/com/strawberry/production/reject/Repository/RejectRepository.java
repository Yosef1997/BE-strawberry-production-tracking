package com.strawberry.production.reject.Repository;

import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RejectRepository extends JpaRepository<Reject, Long> {
    List<Reject> findByWeekNumber(Weather weekNumber);
}
