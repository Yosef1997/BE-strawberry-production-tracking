package com.strawberry.production.weather.dto;

import lombok.Data;

@Data
public class WeatherResponseDto {
    private Long id;
    private Integer weekNumber;
    private Double humidity;
    private Double rainFall;
    private Double temperature;
}
