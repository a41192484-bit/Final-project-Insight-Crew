package com.insightcrew.domain.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TripRankingResponse {
    private String title;
    private String imageUrl;
    private int score;           // 날씨 기반 점수
}
