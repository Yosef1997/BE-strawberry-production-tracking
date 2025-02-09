package com.strawberry.production.report.dto;

import com.strawberry.production.report.entity.Report;
import com.strawberry.production.users.entity.Users;
import lombok.Data;

@Data
public class ReportRequestDto {
    private Long id;
    private Long userId;
    private Double grossStrawberryWeight;
    private Integer packAQuantity;
    private Integer packBQuantity;
    private Integer packCQuantity;
    private Double rejectWeight;

    public Report toEntity(Users pic) {
        Report report = new Report();
        report.setId(id);
        report.setPic(pic);
        report.setGrossStrawberryWeight(grossStrawberryWeight);
        report.setPackAQuantity(packAQuantity);
        report.setPackBQuantity(packBQuantity);
        report.setPackCQuantity(packCQuantity);
        report.setRejectWeight(rejectWeight);
        return report;
    }
}
