package com.insightcrew.domain.trip.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TripVo {
    private Long tripId;
    private String title;
    private String category;
    private String address;
    private String imageUrl;
    private String mapX;
    private String mapY;
    private String apiContentId;   // 관광공사 contentId
}
