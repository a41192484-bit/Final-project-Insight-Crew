package com.insightcrew.domain.weather.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WeeklyWeatherDto {

    private LocalDate date;

    private int minTemp;
    private int maxTemp;

    private int rainPercent;
    private int windSpeed;
    private int cloudPercent;

    private String weatherText;
    private String iconCode;

    /** 화면용 아이콘 URL */
    public String getIconUrl() {
        return "https://openweathermap.org/img/wn/"
             + iconCode
             + "@2x.png";
    }
}
