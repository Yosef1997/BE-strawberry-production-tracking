package com.strawberry.production.reject.service.impl;

import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.reject.Repository.RejectRepository;
import com.strawberry.production.reject.dto.RejectRequestDto;
import com.strawberry.production.reject.dto.RejectResponseDto;
import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.reject.service.RejectService;
import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.weather.service.WeatherService;
import com.strawberry.production.weather.service.impl.WeatherServiceImpl;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class RejectServiceImpl implements RejectService {
    private final RejectRepository rejectRepository;
    private final WeatherService weatherService;

    public RejectServiceImpl(RejectRepository rejectRepository, WeatherService weatherService) {
        this.rejectRepository = rejectRepository;
        this.weatherService = weatherService;
    }


    @Override
    public List<RejectResponseDto> getAllRejects() {
        return rejectRepository.findAll().stream().map(this::mapToRejectResponseDto).toList();
    }

    @Override
    public Reject getDetailReject(Long id) {
        return rejectRepository.findById(id).orElseThrow(()->new NotFoundException("Reject data with id: " + id + " not found"));
    }

    @Override
    public RejectResponseDto createReject(RejectRequestDto rejectRequestDto) {
        Weather weather = weatherService.getDetailWeather(rejectRequestDto.getWeekNumberId());
        Reject reject = rejectRepository.save(rejectRequestDto.toEntity(weather));
        return mapToRejectResponseDto(reject);
    }

    @Override
    public RejectResponseDto editReject(RejectRequestDto rejectRequestDto) {
        Weather weather = weatherService.getDetailWeather(rejectRequestDto.getWeekNumberId());
        Reject reject = rejectRepository.findById(rejectRequestDto.getId()).orElseThrow(()->new NotFoundException("Yield data with id: " + rejectRequestDto.getId() + " not found"));
        reject.setWeekNumber(weather);
        reject.setRejectDueToPest(rejectRequestDto.getRejectDueToPest());
        reject.setRejectDueToDisease(rejectRequestDto.getRejectDueToDisease());
        Reject edited = rejectRepository.save(reject);
        return mapToRejectResponseDto(edited);
    }

    @Override
    public List<Reject> getRejectByWeekNumber(Weather weekNumber) {
        return rejectRepository.findByWeekNumber(weekNumber);
    }

    public RejectResponseDto mapToRejectResponseDto(Reject reject) {
        RejectResponseDto response = new RejectResponseDto();
        response.setId(reject.getId());
        response.setWeekNumber(reject.getWeekNumber().getWeekNumber());
        response.setRejectDueToPest(reject.getRejectDueToPest());
        response.setRejectDueToDisease(reject.getRejectDueToDisease());
        return response;
    }
}
