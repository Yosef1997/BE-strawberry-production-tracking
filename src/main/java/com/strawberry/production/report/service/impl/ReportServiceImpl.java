package com.strawberry.production.report.service.impl;

import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.report.dto.ReportRequestDto;
import com.strawberry.production.report.dto.ReportResponseDto;
import com.strawberry.production.report.entity.Report;
import com.strawberry.production.report.repository.ReportRepository;
import com.strawberry.production.report.service.ReportService;
import com.strawberry.production.users.entity.Users;
import com.strawberry.production.users.service.impl.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final ReportRepository reportRepository;
    private final UsersServiceImpl usersService;

    public ReportServiceImpl(ReportRepository reportRepository, UsersServiceImpl usersService) {
        this.reportRepository = reportRepository;
        this.usersService = usersService;
    }

    @Override
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll().stream().map(this::mapToReportResponseDto).toList();
    }

    @Override
    public ReportResponseDto createReport(ReportRequestDto reportRequestDto) {
        Users pic = usersService.getDetailUserId(reportRequestDto.getUserId());
        Report newReport = reportRepository.save(reportRequestDto.toEntity(pic));
        return mapToReportResponseDto(newReport);
    }

    @Override
    public ReportResponseDto updateReport(ReportRequestDto reportRequestDto) {
        Report report = reportRepository.findById(reportRequestDto.getId()).orElseThrow(() -> new NotFoundException("Report with id: " + reportRequestDto.getId() + " not found"));
        report.setPic(usersService.getDetailUserId(reportRequestDto.getUserId()));
        report.setGrossStrawberryWeight(reportRequestDto.getGrossStrawberryWeight());
        report.setPackAQuantity(reportRequestDto.getPackAQuantity());
        report.setPackBQuantity(reportRequestDto.getPackBQuantity());
        report.setPackCQuantity(reportRequestDto.getPackCQuantity());
        report.setRejectWeight(reportRequestDto.getRejectWeight());
        Report updated = reportRepository.save(report);
        return mapToReportResponseDto(updated);
    }

    @Override
    public String deleteReport(ReportRequestDto reportRequestDto) {
        logger.info("Delete attempt for report: {}", reportRequestDto.getId());
        Report report = reportRepository.findById(reportRequestDto.getId()).orElseThrow(() -> new NotFoundException("Report with id: " + reportRequestDto.getId() + " not found"));
        if (report != null){
            report.setDeletedAt(Instant.now());
            reportRepository.save(report);
            return "Report with id: " + reportRequestDto.getId() + " have deleted successfully";
        }
        return "Delete report failed";
    }

    public ReportResponseDto mapToReportResponseDto(Report report) {
        ReportResponseDto response = new ReportResponseDto();
        response.setId(report.getId());
        response.setPic(report.getPic().getName());
        response.setGrossStrawberryWeight(report.getGrossStrawberryWeight());
        response.setPackAQuantity(report.getPackAQuantity());
        response.setPackBQuantity(report.getPackBQuantity());
        response.setPackCQuantity(report.getPackCQuantity());
        response.setRejectWeight(report.getRejectWeight());
        response.setCreatedAt(report.getCreatedAt());
        response.setDeletedAt(report.getDeletedAt());
        return response;
    }
}
