package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class DashboardRequestDto {
    private Instant startDate;
    private Instant endDate;
}
