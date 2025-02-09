package com.strawberry.production.report.controller;

import com.strawberry.production.report.dto.DashboardRequestDto;
import com.strawberry.production.report.dto.DashboardResponseDto;
import com.strawberry.production.report.dto.ReportRequestDto;
import com.strawberry.production.report.dto.ReportResponseDto;
import com.strawberry.production.report.service.ReportService;
import com.strawberry.production.response.Response;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@Validated
@Log
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<Response<List<ReportResponseDto>>> getAllReport() {
        return Response.successResponse("Get all report success", reportService.getAllReports());
    }

    @PostMapping
    public ResponseEntity<Response<ReportResponseDto>> createReport(@RequestBody ReportRequestDto reportRequestDto) {
        return Response.successResponse("Create report success", reportService.createReport(reportRequestDto));
    }

    @PutMapping("/update-report")
    public ResponseEntity<Response<ReportResponseDto>> updateReport(@RequestBody ReportRequestDto reportRequestDto) {
        return Response.successResponse("Update report success", reportService.updateReport(reportRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<Response<String>> deleteReport(@RequestBody ReportRequestDto reportRequestDto) {
        return Response.successResponse("Delete report success", reportService.deleteReport(reportRequestDto));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Response<DashboardResponseDto>> getDashboard(@RequestBody DashboardRequestDto dashboardRequestDto) {
        return Response.successResponse("Get dashboard success", reportService.getDashboardData(dashboardRequestDto));
    }
}
