package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RejectRatio {
    private LocalDateTime timeFrame;
    private boolean isHourly; // true for hourly, false for daily
    private double rejectRatio; // (Reject kg / Berat kg) x 100%
}
