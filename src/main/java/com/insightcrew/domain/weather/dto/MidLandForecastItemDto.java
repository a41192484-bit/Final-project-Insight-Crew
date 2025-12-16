package com.insightcrew.domain.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MidLandForecastItemDto {

    // 강수확률
    private Integer rnSt4Am;
    private Integer rnSt4Pm;
    private Integer rnSt5Am;
    private Integer rnSt5Pm;
    private Integer rnSt6Am;
    private Integer rnSt6Pm;
    private Integer rnSt7Am;
    private Integer rnSt7Pm;

    // 하늘상태
    private String wf4Am;
    private String wf4Pm;
    private String wf5Am;
    private String wf5Pm;
    private String wf6Am;
    private String wf6Pm;
    private String wf7Am;
    private String wf7Pm;
}
