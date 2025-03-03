package com.strawberry.production.analysis.service;

import com.strawberry.production.analysis.dto.AllDataDto;
import com.strawberry.production.analysis.dto.AnalysisResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnalysisService {
    Page<AnalysisResultDto> getWeeklyAnalysis(Pageable pageable);

    AnalysisResultDto getBestWeatherAnalysis();

    Page<AllDataDto> getAllWeatherData(Pageable pageable, String sortBy, String sortDirection);
}
