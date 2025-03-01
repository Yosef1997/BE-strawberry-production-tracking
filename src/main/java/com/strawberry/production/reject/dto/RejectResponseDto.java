package com.strawberry.production.reject.dto;

import lombok.Data;

@Data
public class RejectResponseDto {
    private Long id;
    private Integer weekNumber;
    private Double rejectDueToPest;
    private Double rejectDueToDisease;
}
