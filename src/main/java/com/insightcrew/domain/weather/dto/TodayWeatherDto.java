package com.insightcrew.domain.weather.dto;

import lombok.Data;

@Data
public class TodayWeatherDto {

    private Integer temp;   // 현재 기온
    private Double wind;    // 풍속
    private Integer rain;   // 강수확률
    private String sky;     // 하늘상태
}
