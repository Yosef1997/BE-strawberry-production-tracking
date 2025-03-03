package com.strawberry.production.analysis.service.impl;

import com.strawberry.production.analysis.dto.AllDataDto;
import com.strawberry.production.analysis.dto.AnalysisResultDto;
import com.strawberry.production.analysis.service.AnalysisService;
import com.strawberry.production.reject.Repository.RejectRepository;
import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.weather.repository.WeatherRepository;
import com.strawberry.production.yield.entity.Yield;
import com.strawberry.production.yield.repository.YieldRepository;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class AnalysisServiceImpl implements AnalysisService {
    private final WeatherRepository weatherRepository;
    private final YieldRepository yieldRepository;
    private final RejectRepository rejectRepository;

    public AnalysisServiceImpl(WeatherRepository weatherRepository, YieldRepository yieldRepository, RejectRepository rejectRepository) {
        this.weatherRepository = weatherRepository;
        this.yieldRepository = yieldRepository;
        this.rejectRepository = rejectRepository;
    }

    public Page<AnalysisResultDto> getWeeklyAnalysis(Pageable pageable) {
        Page<Weather> weatherPage = weatherRepository.findAll(pageable);

        List<AnalysisResultDto> result = weatherPage.stream().map(weather -> {
            List<Yield> yields = yieldRepository.findByWeekNumber(weather);
            List<Reject> rejects = rejectRepository.findByWeekNumber(weather);

            return new AnalysisResultDto(weather, yields, rejects);
        }).toList();

        return new PageImpl<>(result, pageable, weatherPage.getTotalElements());
    }

    public AnalysisResultDto getBestWeatherAnalysis() {
        List<Weather> bestWeatherList = weatherRepository.findBestWeather(PageRequest.of(0, 1));

        if (bestWeatherList.isEmpty()) {
            return null;
        }

        Weather bestWeather = bestWeatherList.getFirst();

        // Explicitly initialize lazy-loaded collections
        List<Yield> bestYields = bestWeather.getYield();
        List<Reject> bestRejects = bestWeather.getRejectRecords();

        return new AnalysisResultDto(bestWeather, bestYields, bestRejects);
    }

    @Override
    public Page<AllDataDto> getAllWeatherData(Pageable pageable, String sortBy, String sortDirection) {
        if (sortBy == null) {
            return weatherRepository.findAllWeatherDataWithYieldAndReject(pageable);
        }

        switch (sortBy.toLowerCase()) {
            case "yield":
                if ("asc".equalsIgnoreCase(sortDirection)) {
                    return weatherRepository.findAllWeatherDataSortByYieldAsc(pageable);
                } else {
                    return weatherRepository.findAllWeatherDataSortByYieldDesc(pageable);
                }
            case "reject":
                if ("asc".equalsIgnoreCase(sortDirection)) {
                    return weatherRepository.findAllWeatherDataSortByTotalRejectAsc(pageable);
                } else {
                    return weatherRepository.findAllWeatherDataSortByTotalRejectDesc(pageable);
                }
            default:
                return weatherRepository.findAllWeatherDataWithYieldAndReject(pageable);
        }
//        return weatherRepository.findAllWeatherDataWithYieldAndReject(pageable);
    }
}
