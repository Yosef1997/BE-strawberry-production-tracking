package com.strawberry.production.reject.service;

import com.strawberry.production.reject.dto.RejectRequestDto;
import com.strawberry.production.reject.dto.RejectResponseDto;
import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.weather.entity.Weather;

import java.util.List;

public interface RejectService {
    List<RejectResponseDto> getAllRejects();

    Reject getDetailReject(Long id);

    RejectResponseDto createReject(RejectRequestDto rejectRequestDto);

    RejectResponseDto editReject(RejectRequestDto rejectRequestDto);

    List<Reject> getRejectByWeekNumber(Weather weekNumber);
}
