package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PackRatio {
    private Instant timeFrame;
    private boolean isHourly;
    private double packARatio;
    private double packBRatio;
    private double packCRatio;
}
