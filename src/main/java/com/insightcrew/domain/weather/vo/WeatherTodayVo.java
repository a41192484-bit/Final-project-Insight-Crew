package com.insightcrew.domain.weather.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherTodayVo {

    private Integer regionId;   // PK (FK → region.id)

    // 오늘
    private Double temp;
    private Integer sky;         // ⭐ 수정
    private Integer pty;
    private Double wind;

    private LocalDateTime updatedAt;   // ⭐ 수정

    // 단기예보
    private Integer tomorrowHigh;
    private Integer tomorrowLow;
    private String tomorrowSky;
    private Integer tomorrowRain;

    private Integer day2High;
    private Integer day2Low;
    private String day2Sky;
    private Integer day2Rain;

    private Integer day3High;
    private Integer day3Low;
    private String day3Sky;
    private Integer day3Rain;
}
