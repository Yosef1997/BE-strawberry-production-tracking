package com.strawberry.production.report.service;

import com.strawberry.production.report.dto.DashboardRequestDto;
import com.strawberry.production.report.dto.DashboardResponseDto;
import com.strawberry.production.report.dto.ReportRequestDto;
import com.strawberry.production.report.dto.ReportResponseDto;

import java.util.List;

public interface ReportService {
    List<ReportResponseDto> getAllReports();

    ReportResponseDto createReport(ReportRequestDto reportRequestDto);

    ReportResponseDto updateReport(ReportRequestDto reportRequestDto);

    String deleteReport(ReportRequestDto reportRequestDto);

    DashboardResponseDto getDashboardData(DashboardRequestDto dashboardRequestDto);
}
