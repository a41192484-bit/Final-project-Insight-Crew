package com.insightcrew.domain.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherItemDto {

    private String category;      // PTY, T1H, SKY, WSD ...

    // 단기예보용
    private String fcstValue;
    private String fcstDate;
    private String fcstTime;

    // 초단기 실황용
    private String obsrValue;

    // 랭킹에서 쓸 공통 getter
    public String getValue() {
        if (fcstValue != null && !fcstValue.isBlank()) {
            return fcstValue;
        }
        return obsrValue;
    }
}
