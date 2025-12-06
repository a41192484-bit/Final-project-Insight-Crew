package com.insightcrew.domain.weather.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherTodayVo {
	private Long weatherId;
	private String regionCode;
	private Double temp;
	private Integer sky;
	private Integer pty;
	private Double wind;
	private String updatedAt;
}
