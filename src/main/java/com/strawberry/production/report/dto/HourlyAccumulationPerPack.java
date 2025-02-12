package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class HourlyAccumulationPerPack {
    private Instant hour;
    private int packAQuantity;
    private int packBQuantity;
    private int packCQuantity;
}
