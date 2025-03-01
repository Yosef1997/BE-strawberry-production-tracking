package com.strawberry.production.analysis.service;

import com.strawberry.production.analysis.dto.AnalysisResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnalysisService {
    Page<AnalysisResultDto> getWeeklyAnalysis(Pageable pageable);

    AnalysisResultDto getBestWeatherAnalysis();
}
