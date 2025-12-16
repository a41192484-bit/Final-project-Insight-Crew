package com.insightcrew.domain.trip.dto;

import com.insightcrew.domain.trip.enums.TripCategory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TripResponse {

    private String contentId;        // 상세 조회용 ID
    private String name;             // 여행지명
    private String fullAddress;      // 주소
    private String imageUrl;         // 대표 이미지
    
    private TripCategory category;   // 카테고리 (NATURE/CULTURE/....)
    private String regionCode;       // 지역 코드

    private double lat;              // 위도
    private double lon;              // 경도
}
