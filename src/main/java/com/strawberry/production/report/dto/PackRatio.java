package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PackRatio {
    private LocalDateTime timeFrame;
    private boolean isHourly;
    private double packARatio;
    private double packBRatio;
    private double packCRatio;
}
