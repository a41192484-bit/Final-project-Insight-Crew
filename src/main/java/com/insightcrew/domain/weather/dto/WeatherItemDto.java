package com.insightcrew.domain.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherItemDto {
	private String category; // PTY, T1H, SKY...
	private String fcstValue;
}
