package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HourlyAccumulationPerPack {
    private String packType; // "A", "B", or "C"
    private LocalDateTime hour;
    private int quantity;
}
