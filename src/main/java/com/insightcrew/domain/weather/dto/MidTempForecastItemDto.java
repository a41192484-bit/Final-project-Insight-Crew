package com.insightcrew.domain.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MidTempForecastItemDto {

    private String taMin4;
    private String taMax4;
    private String taMin5;
    private String taMax5;
    private String taMin6;
    private String taMax6;
    private String taMin7;
    private String taMax7;
}
