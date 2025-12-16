package com.insightcrew.domain.trip.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripVo {

    private Long tripId;

    private String contentId;
    private String name;
    private String category;

    private String sido;     
    private String sigungu;    

    private String fullAddress;
    private String imageUrl;
    private String detailImageUrl;

    private Double lat;
    private Double lon;

    private String regionCode;     // 관광공사 지역 코드 (예전 API용 — 필요하면 유지)
    private String contentTypeId;

    private String tel;
    private String homepage;
    private String description;

    private Integer regionId;      // 날씨 지역코드 매핑용

    /** no-image 처리 */
    public String getImageUrl() {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return "/images/no-image.png";
        }
        return imageUrl;
    }
}
