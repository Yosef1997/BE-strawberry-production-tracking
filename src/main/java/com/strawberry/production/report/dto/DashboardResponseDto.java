package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DashboardResponseDto {
    private List<HourlyAccumulationPerPIC> hourlyAccumulationPerPIC;
    private List<HourlyAccumulationPerPack> hourlyAccumulationPerPack;
    private List<ProductivityMetrics> productivityMetrics;
    private List<RejectRatio> rejectRatios;
    private List<PackRatio> packRatios;
}

