package com.strawberry.production.yield.service;

import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.yield.dto.YieldRequestDto;
import com.strawberry.production.yield.dto.YieldResponseDto;
import com.strawberry.production.yield.entity.Yield;

import java.util.List;

public interface YieldService {
    List<YieldResponseDto> getAllYields();

    Yield getDetailYield(Long id);

    YieldResponseDto createYield(YieldRequestDto yieldRequestDto);

    YieldResponseDto editYield(YieldRequestDto yieldRequestDto);

    List<Yield> getYieldByWeekNumber(Weather weekNumber);
}
