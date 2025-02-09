package com.strawberry.production.report.repository;

import com.strawberry.production.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCreatedAtBetween(Instant startDate, Instant endDate);
}
