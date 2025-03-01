package com.strawberry.production.weather.service;

import com.strawberry.production.weather.dto.WeatherRequestDto;
import com.strawberry.production.weather.dto.WeatherResponseDto;
import com.strawberry.production.weather.entity.Weather;

import java.util.List;

public interface WeatherService {
    List<WeatherResponseDto> getAllWeathers();

    Weather getDetailWeather(Long id);

    WeatherResponseDto createWeatherData(WeatherRequestDto weatherRequestDto);

    WeatherResponseDto editWeatherData(WeatherRequestDto weatherRequestDto);
}
