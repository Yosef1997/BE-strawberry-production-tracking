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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
        if (report != null){
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
                        record -> Map.entry(
                                record.getPic().getName(),
                                record.getCreatedAt().truncatedTo(ChronoUnit.HOURS)
                        ),
                        Collectors.summingInt(record ->
                                record.getPackAQuantity() +
                                        record.getPackBQuantity() +
                                        record.getPackCQuantity()
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                    HourlyAccumulationPerPIC acc = new HourlyAccumulationPerPIC();
                    acc.setPicName(entry.getKey().getKey());
                    acc.setHour(LocalDateTime.ofInstant(entry.getKey().getValue(), ZoneId.systemDefault()));
                    acc.setTotalQuantity(entry.getValue());
                    return acc;
                })
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
        List<HourlyAccumulationPerPack> result = records.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getCreatedAt().truncatedTo(ChronoUnit.HOURS)
                ))
                .entrySet().stream()
                .flatMap(entry -> {
                    LocalDateTime hour = LocalDateTime.ofInstant(entry.getKey(), ZoneId.systemDefault());
                    List<Report> hourlyRecords = entry.getValue();

                    // Calculate sum for Pack A
                    HourlyAccumulationPerPack packA = new HourlyAccumulationPerPack();
                    packA.setPackType("A");
                    packA.setHour(hour);
                    packA.setQuantity(hourlyRecords.stream()
                            .mapToInt(Report::getPackAQuantity)
                            .sum());

                    // Calculate sum for Pack B
                    HourlyAccumulationPerPack packB = new HourlyAccumulationPerPack();
                    packB.setPackType("B");
                    packB.setHour(hour);
                    packB.setQuantity(hourlyRecords.stream()
                            .mapToInt(Report::getPackBQuantity)
                            .sum());

                    // Calculate sum for Pack C
                    HourlyAccumulationPerPack packC = new HourlyAccumulationPerPack();
                    packC.setPackType("C");
                    packC.setHour(hour);
                    packC.setQuantity(hourlyRecords.stream()
                            .mapToInt(Report::getPackCQuantity)
                            .sum());

                    return List.of(packA, packB, packC).stream();
                })
                .collect(Collectors.toList());

        return result;
    }

    private List<ProductivityMetrics> calculateProductivityMetrics(List<Report> records) {
        // Group by PIC and date
        Map<Map.Entry<String, LocalDateTime>, List<Report>> groupedRecords = records.stream()
                .collect(Collectors.groupingBy(
                        record -> Map.entry(
                                record.getPic().getName(),
                                LocalDateTime.ofInstant(record.getCreatedAt().truncatedTo(ChronoUnit.DAYS), ZoneId.systemDefault())
                        )
                ));

        return groupedRecords.entrySet().stream()
                .map(entry -> {
                    String picName = entry.getKey().getKey();
                    LocalDateTime date = entry.getKey().getValue();
                    List<Report> picDayRecords = entry.getValue();

                    // Calculate total packs for the day
                    int totalPacks = picDayRecords.stream()
                            .mapToInt(record ->
                                    record.getPackAQuantity() +
                                            record.getPackBQuantity() +
                                            record.getPackCQuantity()
                            )
                            .sum();

                    ProductivityMetrics metrics = new ProductivityMetrics();
                    metrics.setPicName(picName);
                    metrics.setDate(date);

                    // Calculate hourly productivity (packs per hour)
                    metrics.setHourlyProductivity((double) totalPacks / 60); // per minute

                    // Calculate daily productivity (packs per day based on 600 minutes)
                    metrics.setDailyProductivity((double) totalPacks / 600);

                    return metrics;
                })
                .collect(Collectors.toList());
    }

    private List<RejectRatio> calculateRejectRatios(List<Report> records) {
        // Grouping by Date and Hour
        Map<Map.Entry<LocalDateTime, Integer>, List<Report>> groupedRecords = records.stream()
                .collect(Collectors.groupingBy(
                        record -> Map.entry(
                                LocalDateTime.ofInstant(record.getCreatedAt().truncatedTo(ChronoUnit.DAYS), ZoneId.systemDefault()),
                                record.getCreatedAt().atZone(ZoneId.systemDefault()).getHour()
                        )
                ));

        return groupedRecords.entrySet().stream()
                .map(entry -> {
                    LocalDateTime date = entry.getKey().getKey();
                    int hour = entry.getKey().getValue();
                    List<Report> hourlyReports = entry.getValue();

                    double totalReject = hourlyReports.stream().mapToDouble(Report::getRejectWeight).sum();
                    double totalWeight = hourlyReports.stream().mapToDouble(Report::getGrossStrawberryWeight).sum();

                    RejectRatio ratio = new RejectRatio();
                    ratio.setTimeFrame(date.withHour(hour)); // Menggabungkan tanggal dan jam
                    ratio.setHourly(true);
                    ratio.setRejectRatio(totalWeight > 0 ? (totalReject / totalWeight) * 100 : 0);

                    return ratio;
                })
                .collect(Collectors.toList());
    }

    private List<PackRatio> calculatePackRatios(List<Report> records) {
        // Grouping by Date and Hour
        Map<Map.Entry<LocalDateTime, Integer>, List<Report>> groupedRecords = records.stream()
                .collect(Collectors.groupingBy(
                        record -> Map.entry(
                                LocalDateTime.ofInstant(record.getCreatedAt().truncatedTo(ChronoUnit.DAYS), ZoneId.systemDefault()),
                                record.getCreatedAt().atZone(ZoneId.systemDefault()).getHour()
                        )
                ));

        return groupedRecords.entrySet().stream()
                .map(entry -> {
                    LocalDateTime date = entry.getKey().getKey();
                    int hour = entry.getKey().getValue();
                    List<Report> hourlyReports = entry.getValue();

                    int totalPacks = hourlyReports.stream().mapToInt(record ->
                            record.getPackAQuantity() +
                                    record.getPackBQuantity() +
                                    record.getPackCQuantity()
                    ).sum();

                    double packATotal = hourlyReports.stream().mapToInt(Report::getPackAQuantity).sum();
                    double packBTotal = hourlyReports.stream().mapToInt(Report::getPackBQuantity).sum();
                    double packCTotal = hourlyReports.stream().mapToInt(Report::getPackCQuantity).sum();

                    PackRatio ratio = new PackRatio();
                    ratio.setTimeFrame(date.withHour(hour)); // Menggabungkan tanggal dan jam
                    ratio.setHourly(true);
                    ratio.setPackARatio(totalPacks > 0 ? (packATotal / (double) totalPacks) * 100 : 0);
                    ratio.setPackBRatio(totalPacks > 0 ? (packBTotal / (double) totalPacks) * 100 : 0);
                    ratio.setPackCRatio(totalPacks > 0 ? (packCTotal / (double) totalPacks) * 100 : 0);

                    return ratio;
                })
                .collect(Collectors.toList());
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
