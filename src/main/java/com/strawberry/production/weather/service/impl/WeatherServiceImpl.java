package com.strawberry.production.weather.service.impl;

import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.weather.dto.WeatherRequestDto;
import com.strawberry.production.weather.dto.WeatherResponseDto;
import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.weather.repository.WeatherRepository;
import com.strawberry.production.weather.service.WeatherService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;

    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public List<WeatherResponseDto> getAllWeathers() {
        return weatherRepository.findAll().stream().map(this::mapToWeatherResponseDto).toList();
    }

    @Override
    public Weather getDetailWeather(Long id) {
        return weatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Weather data with week number: " + id + " not found"));
    }

    @Override
    public WeatherResponseDto createWeatherData(WeatherRequestDto weatherRequestDto) {
        Weather weather = weatherRepository.save(weatherRequestDto.toEntity());
        return mapToWeatherResponseDto(weather);
    }

    @Override
    public WeatherResponseDto editWeatherData(WeatherRequestDto weatherRequestDto) {
        Weather detail = weatherRepository.findById(weatherRequestDto.getId()).orElseThrow(() -> new NotFoundException("Weather data with week number: " + weatherRequestDto.getId() + " not found"));
        detail.setHumidity(weatherRequestDto.getHumidity());
        detail.setRainFall(weatherRequestDto.getRainFall());
        detail.setTemperature(weatherRequestDto.getTemperature());
        Weather edited = weatherRepository.save(detail);
        return mapToWeatherResponseDto(edited);
    }

    public WeatherResponseDto mapToWeatherResponseDto(Weather weather) {
        WeatherResponseDto response = new WeatherResponseDto();
        response.setId(weather.getId());
        response.setWeekNumber(weather.getWeekNumber());
        response.setHumidity(weather.getHumidity());
        response.setRainFall(weather.getRainFall());
        response.setTemperature(weather.getTemperature());

        return response;
    }
}
