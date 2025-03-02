package com.strawberry.production.analysis.controller;

import com.strawberry.production.analysis.dto.AnalysisResultDto;
import com.strawberry.production.analysis.service.AnalysisService;
import com.strawberry.production.response.Response;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analysis")
@Validated
@Log
public class AnalysisController {
    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping
    public ResponseEntity<Response<Page<AnalysisResultDto>>> getWeeklyAnalysis(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        String sortByField = (sortBy != null) ? sortBy : "weekNumber";
        Sort.Direction direction = (sortDir != null && sortDir.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(direction, sortByField));
        return Response.successResponse(analysisService.getWeeklyAnalysis(pageable));
    }

    @GetMapping("/best-weather")
    public ResponseEntity<Response<AnalysisResultDto>> getBestWeatherAnalysis() {
        return Response.successResponse(analysisService.getBestWeatherAnalysis());
    }
}
