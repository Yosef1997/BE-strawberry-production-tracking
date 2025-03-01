package com.strawberry.production.analysis.dto;

import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.weather.entity.Weather;
import com.strawberry.production.yield.entity.Yield;
import lombok.Data;

import java.util.List;

@Data
public class AnalysisResultDto {
    private Integer weekNumber;
    private Double humidity;
    private Double rainFall;
    private Double temperature;
    private Double totalYield;
    private Double totalRejectDueToPest;
    private Double totalRejectDueToDisease;

    public AnalysisResultDto(Weather weather, List<Yield> yields, List<Reject> rejects) {
        this.weekNumber = weather.getWeekNumber();
        this.humidity = weather.getHumidity();
        this.rainFall = weather.getRainFall();
        this.temperature = weather.getTemperature();

        this.totalYield = yields.stream().mapToDouble(Yield::getStrawberryYield).sum();
        this.totalRejectDueToPest = rejects.stream().mapToDouble(Reject::getRejectDueToPest).sum();
        this.totalRejectDueToDisease = rejects.stream().mapToDouble(Reject::getRejectDueToDisease).sum();
    }
}
