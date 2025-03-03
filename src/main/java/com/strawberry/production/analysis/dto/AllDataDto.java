package com.strawberry.production.analysis.dto;

import lombok.Data;

@Data
public class AllDataDto {
    private Integer weekNumber;
    private Double humidity;
    private Double rainFall;
    private Double temperature;
    private Double totalYield;
    private Double rejectDueToPest;
    private Double rejectDueToDisease;

    // Constructor used by JPQL queries
    public AllDataDto(Long id, Integer weekNumber, Double humidity, Double rainFall,
                          Double temperature, Double totalYield, Double rejectDueToPest,
                          Double rejectDueToDisease) {
        this.weekNumber = weekNumber;
        this.humidity = humidity;
        this.rainFall = rainFall;
        this.temperature = temperature;
        this.totalYield = totalYield;
        this.rejectDueToPest = rejectDueToPest;
        this.rejectDueToDisease = rejectDueToDisease;
    }

    public Double getTotalReject() {
        return (rejectDueToPest != null ? rejectDueToPest : 0) +
                (rejectDueToDisease != null ? rejectDueToDisease : 0);
    }
}
