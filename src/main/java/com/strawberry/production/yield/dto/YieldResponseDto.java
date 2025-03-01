package com.strawberry.production.yield.dto;

import lombok.Data;

@Data
public class YieldResponseDto {
    private Long id;
    private Integer weekNumber;
    private Double strawberryYield;
}
