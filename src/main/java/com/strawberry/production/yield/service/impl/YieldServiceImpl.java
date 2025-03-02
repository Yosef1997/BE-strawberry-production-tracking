package com.strawberry.production.yield.service.impl;

import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.weather.service.WeatherService;
import com.strawberry.production.yield.dto.YieldRequestDto;
import com.strawberry.production.yield.dto.YieldResponseDto;
import com.strawberry.production.yield.entity.Yield;
import com.strawberry.production.yield.repository.YieldRepository;
import com.strawberry.production.yield.service.YieldService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class YieldServiceImpl implements YieldService {
    private final YieldRepository yieldRepository;
    private final WeatherService weatherService;

    public YieldServiceImpl(YieldRepository yieldRepository, WeatherService weatherService) {
        this.yieldRepository = yieldRepository;
        this.weatherService = weatherService;
    }

    @Override
    public List<YieldResponseDto> getAllYields() {
        return yieldRepository.findAll().stream().map(this::mapToYieldResponseDto).toList();
    }

    @Override
    public Yield getDetailYield(Long id) {
        return yieldRepository.findById(id).orElseThrow(() -> new NotFoundException("Yield data with id: " + id + " not found"));
    }

    @Override
    public YieldResponseDto createYield(YieldRequestDto yieldRequestDto) {
        Weather weather = weatherService.getDetailWeather(yieldRequestDto.getWeekNumberId());
        Yield yield = yieldRepository.save(yieldRequestDto.toEntity(weather));
        return mapToYieldResponseDto(yield);
    }

    @Override
    public YieldResponseDto editYield(YieldRequestDto yieldRequestDto) {
        Yield yield = yieldRepository.findById(yieldRequestDto.getId()).orElseThrow(() -> new NotFoundException("Yield data with id: " + yieldRequestDto.getId() + " not found"));
        Weather weather = weatherService.getDetailWeather(yieldRequestDto.getWeekNumberId());
        yield.setWeekNumber(weather);
        yield.setStrawberryYield(yieldRequestDto.getStrawberryYield());
        Yield edited = yieldRepository.save(yield);
        return mapToYieldResponseDto(edited);
    }

    @Override
    public List<Yield> getYieldByWeekNumber(Weather weekNumber) {
        return yieldRepository.findByWeekNumber(weekNumber);
    }


    public YieldResponseDto mapToYieldResponseDto(Yield yield) {
        YieldResponseDto response = new YieldResponseDto();
        response.setId(yield.getId());
        response.setWeekNumber(yield.getWeekNumber().getWeekNumber());
        response.setStrawberryYield(yield.getStrawberryYield());
        return response;
    }
}
