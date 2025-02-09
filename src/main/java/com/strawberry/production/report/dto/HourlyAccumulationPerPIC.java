package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HourlyAccumulationPerPIC {
    private String picName;
    private LocalDateTime hour;
    private int totalQuantity;
}
