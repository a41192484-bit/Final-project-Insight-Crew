package com.insightcrew.domain.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TripRankingDto {

    private int rankNo;          // 랭킹 순위
    private Long tripId;         // 여행지 ID
    private String title;        // 여행지 이름
    private String imageUrl;     // 대표 이미지
    
    private int score;           // 점수
    private Double temperature;  // 기온
    private String weather;      // 날씨 텍스트
    
    private String region;       // 지역 필터링용
    private String reason;       // 추천 이유
    
}
