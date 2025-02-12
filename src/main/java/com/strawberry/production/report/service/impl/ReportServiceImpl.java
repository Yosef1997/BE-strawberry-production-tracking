package com.strawberry.production.report.service.impl;

import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.report.dto.*;
import com.strawberry.production.report.entity.Report;
import com.strawberry.production.report.repository.ReportRepository;
import com.strawberry.production.report.service.ReportService;
import com.strawberry.production.users.entity.Users;
import com.strawberry.production.users.service.impl.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (report != null) {
            report.setDeletedAt(Instant.now());
            reportRepository.save(report);
            return "Report with id: " + reportRequestDto.getId() + " have deleted successfully";
        }
        return "Delete report failed";
    }

    @Override
    public DashboardResponseDto getDashboardData(DashboardRequestDto dashboardRequestDto) {
        List<Report> records = reportRepository.findByCreatedAtBetween(dashboardRequestDto.getStartDate(), dashboardRequestDto.getEndDate());
        DashboardResponseDto response = new DashboardResponseDto();
        response.setHourlyAccumulationPerPIC(calculateHourlyAccumulationPerPIC(records));
        response.setHourlyAccumulationPerPack(calculateHourlyAccumulationPerPack(records));
        response.setProductivityMetrics(calculateProductivityMetrics(records));
        response.setRejectRatios(calculateRejectRatios(records));
        response.setPackRatios(calculatePackRatios(records));

        return response;
    }

    private List<HourlyAccumulationPerPIC> calculateHourlyAccumulationPerPIC(List<Report> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> Map.entry(r.getPic().getName(), r.getCreatedAt().truncatedTo(ChronoUnit.HOURS)),
                        Collectors.summingInt(r -> r.getPackAQuantity() + r.getPackBQuantity() + r.getPackCQuantity())
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    HourlyAccumulationPerPIC dto = new HourlyAccumulationPerPIC();
                    dto.setPicName(entry.getKey().getKey());
                    dto.setHour(entry.getKey().getValue());
                    dto.setTotalQuantity(entry.getValue());
                    return dto;
                })
                .sorted(Comparator.comparing(HourlyAccumulationPerPIC::getHour))
                .collect(Collectors.toList());
    }

    private double calculateProductivityPerHour(List<Report> records) {
        int totalPacks = records.stream()
                .mapToInt(record ->
                        record.getPackAQuantity() +
                                record.getPackBQuantity() +
                                record.getPackCQuantity()
                )
                .sum();

        return (double) totalPacks / 60; // per minute
    }

    private List<HourlyAccumulationPerPack> calculateHourlyAccumulationPerPack(List<Report> records) {

        return records.stream()
                .map(r -> {
                    HourlyAccumulationPerPack dto = new HourlyAccumulationPerPack();
                    dto.setHour(r.getCreatedAt().truncatedTo(ChronoUnit.HOURS));
                    dto.setPackAQuantity(r.getPackAQuantity());
                    dto.setPackBQuantity(r.getPackBQuantity());
                    dto.setPackCQuantity(r.getPackCQuantity());
                    return dto;
                })
                .sorted(Comparator.comparing(HourlyAccumulationPerPack::getHour))
                .collect(Collectors.toList());
    }

    private List<ProductivityMetrics> calculateProductivityMetrics(List<Report> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> Map.entry(r.getPic().getName(), r.getCreatedAt().truncatedTo(ChronoUnit.HOURS)),
                        Collectors.summingInt(r -> r.getPackAQuantity() + r.getPackBQuantity() + r.getPackCQuantity())
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    ProductivityMetrics dto = new ProductivityMetrics();
                    dto.setPicName(entry.getKey().getKey());
                    dto.setDate(entry.getKey().getValue());
                    dto.setHourlyProductivity(round(entry.getValue() / 60.0));
                    dto.setDailyProductivity(round(entry.getValue() / 600.0));
                    return dto;
                })
                .sorted(Comparator.comparing(ProductivityMetrics::getDate))
                .collect(Collectors.toList());
    }

    private List<RejectRatio> calculateRejectRatios(List<Report> records) {
        return records.stream()
                .map(r -> {
                    RejectRatio dto = new RejectRatio();
                    dto.setTimeFrame(r.getCreatedAt().truncatedTo(ChronoUnit.HOURS));
                    dto.setRejectRatio(round((r.getRejectWeight() / r.getGrossStrawberryWeight()) * 100));
                    return dto;
                })
                .sorted(Comparator.comparing(RejectRatio::getTimeFrame))
                .collect(Collectors.toList());
    }

    private List<PackRatio> calculatePackRatios(List<Report> records) {
        return records.stream()
                .map(r -> {
                    int totalPack = r.getPackAQuantity() + r.getPackBQuantity() + r.getPackCQuantity();
                    PackRatio dto = new PackRatio();
                    dto.setTimeFrame(r.getCreatedAt().truncatedTo(ChronoUnit.HOURS));
                    dto.setPackARatio(round((r.getPackAQuantity() / (double) totalPack) * 100));
                    dto.setPackBRatio(round((r.getPackBQuantity() / (double) totalPack) * 100));
                    dto.setPackCRatio(round((r.getPackCQuantity() / (double) totalPack) * 100));
                    return dto;
                })
                .sorted(Comparator.comparing(PackRatio::getTimeFrame))
                .collect(Collectors.toList());
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
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
