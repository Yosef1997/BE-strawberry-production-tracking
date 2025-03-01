package com.strawberry.production.weather.controller;

import com.strawberry.production.response.Response;
import com.strawberry.production.weather.dto.WeatherRequestDto;
import com.strawberry.production.weather.dto.WeatherResponseDto;
import com.strawberry.production.weather.service.WeatherService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@Validated
@Log
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<Response<List<WeatherResponseDto>>> getAllWeathers() {
        return Response.successResponse("All weathers", weatherService.getAllWeathers());
    }

    @PostMapping
    public ResponseEntity<Response<WeatherResponseDto>> createWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
        return Response.successResponse("Create weather data success", weatherService.createWeatherData(weatherRequestDto));
    }

    @PutMapping
    public ResponseEntity<Response<WeatherResponseDto>> editWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
        return Response.successResponse("Edit weather data success", weatherService.editWeatherData(weatherRequestDto));
    }
}
