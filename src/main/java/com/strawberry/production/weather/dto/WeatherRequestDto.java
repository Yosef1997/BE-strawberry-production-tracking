package com.strawberry.production.weather.dto;

import com.strawberry.production.weather.entity.Weather;
import lombok.Data;

@Data
public class WeatherRequestDto {
    private Long id;
    private Integer weekNumber;
    private Double humidity;
    private Double rainFall;
    private Double temperature;

    public Weather toEntity () {
        Weather weather = new Weather();
        weather.setWeekNumber(weekNumber);
        weather.setHumidity(humidity);
        weather.setRainFall(rainFall);
        weather.setTemperature(temperature);
        return weather;
    }
}
