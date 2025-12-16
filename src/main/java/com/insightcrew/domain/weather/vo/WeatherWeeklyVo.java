package com.insightcrew.domain.weather.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WeatherWeeklyVo {

    private Long wWeatherId;

    private String regionCode;        // SEOUL
    private LocalDate forecastDate;   // 예보 날짜

    private BigDecimal tempHigh;
    private BigDecimal tempLow;

    private String skyAm;
    private String skyPm;

    private Integer rainAm;
    private Integer rainPm;

    private LocalDateTime updatedAt;
}
