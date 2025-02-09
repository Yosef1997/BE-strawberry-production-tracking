package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductivityMetrics {
    private String picName;
    private double hourlyProductivity; // packs per hour
    private double dailyProductivity;  // packs per day (600 minutes)
    private LocalDateTime date;
}
