package com.insightcrew.domain.weather.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class WeeklyWeatherDto {

    private LocalDate date;

    private String sky;          // 대표 하늘상태 (pm 우선)
    private Integer tempHigh;
    private Integer tempLow;

    private Integer rain;        // 대표 강수확률 (max)
}
