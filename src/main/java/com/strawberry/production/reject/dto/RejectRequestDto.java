package com.strawberry.production.reject.dto;

import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.weather.entity.Weather;
import lombok.Data;

@Data
public class RejectRequestDto {
    private Long id;
    private Long weekNumberId;
    private Double rejectDueToPest;
    private Double rejectDueToDisease;

    public Reject toEntity(Weather weather) {
        Reject reject = new Reject();
        reject.setWeekNumber(weather);
        reject.setRejectDueToPest(rejectDueToPest);
        reject.setRejectDueToDisease(rejectDueToDisease);
        return reject;
    }
}
