package com.insightcrew.domain.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TripRankingDto {

    private int rankNo;       // 랭킹 순위 1~5
    private Long tripId;      // 여행지 PK

    private String title;     // 여행지 이름 (trip.name)
    private String imageUrl;  // 대표 이미지 (trip.image_url)

    private int score;        // 날씨 기반 점수
    private String reason;    // 추천 이유 (간단 설명)
}
