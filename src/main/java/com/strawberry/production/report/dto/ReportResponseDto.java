package com.strawberry.production.report.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ReportResponseDto {
    private Long id;
    private String pic;
    private Double grossStrawberryWeight;
    private Integer packAQuantity;
    private Integer packBQuantity;
    private Integer packCQuantity;
    private Double rejectWeight;
    private Instant createdAt;
    private Instant deletedAt;
}
