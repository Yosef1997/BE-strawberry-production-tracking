package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ProductivityMetrics {
    private String picName;
    private double hourlyProductivity;
    private double dailyProductivity;
    private Instant date;
}
