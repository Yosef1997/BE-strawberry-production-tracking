package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class RejectRatio {
    private Instant timeFrame;
    private double rejectRatio;
}
