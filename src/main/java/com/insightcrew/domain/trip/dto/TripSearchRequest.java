package com.insightcrew.domain.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TripSearchRequest {
    private String category;      // "ALL", "NATURE", "FOOD"... 문자열로 받음
    private String regionCode;
    private String keyword;
    private int pageNo = 1;
    private int pageSize = 20;
}
