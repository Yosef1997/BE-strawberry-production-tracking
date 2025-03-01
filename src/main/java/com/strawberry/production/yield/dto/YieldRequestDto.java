package com.strawberry.production.yield.dto;

import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.yield.entity.Yield;
import lombok.Data;

@Data
public class YieldRequestDto {
    private Long id;
    private Long WeekNumberId;
    private Double strawberryYield;

    public Yield toEntity(Weather weather) {
        Yield yield = new Yield();
        yield.setWeekNumber(weather);
        yield.setStrawberryYield(strawberryYield);
        return yield;
    }
}
