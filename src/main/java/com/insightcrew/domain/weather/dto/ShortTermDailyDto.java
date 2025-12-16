package com.insightcrew.domain.weather.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ShortTermDailyDto {

    private LocalDate date;   // 내일 / 모레
    private Integer tempHigh;
    private Integer tempLow;
    private String sky;
    private Integer rain;
}
