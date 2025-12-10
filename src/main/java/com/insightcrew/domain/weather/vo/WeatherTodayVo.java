package com.insightcrew.domain.weather.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherTodayVo {

    private Integer regionId;   // ★ PK: region.id

    private Double temp;        // 현재 기온
    private Integer sky;        // 하늘 상태
    private Integer pty;        // 강수 형태
    private Double wind;        // 풍속

    private String updatedAt;   // DB에서 문자열로 받을 때
}
