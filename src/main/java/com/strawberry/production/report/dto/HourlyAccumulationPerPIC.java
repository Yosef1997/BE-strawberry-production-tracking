package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class HourlyAccumulationPerPIC {
    private String picName;
    private Instant hour;
    private int totalQuantity;
}
