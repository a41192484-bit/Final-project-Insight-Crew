package com.insightcrew.domain.weather.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WeatherItemDto {
    private String category; // PTY, T1H, SKY...
    private String fcstValue;
}
