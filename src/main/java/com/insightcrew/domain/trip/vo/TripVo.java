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
    private String fullAddress;
    private String imageUrl;
    private String detailImageUrl;

    private Double lat;
    private Double lon;

    private String regionCode;     // 관광공사 지역 코드 (1 = 서울, 2 = 인천 ...)
    private String contentTypeId;

    private String tel;
    private String homepage;
    private String description;
    
    private Integer regionId;

    /** no-image 처리 */
    public String getImageUrl() {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return "/images/no-image.png";
        }
        return imageUrl;
    }

    /**
     * ⭐ 지역명 추출 로직
     * regionCode → 시/도 이름 매핑
     */
    public String getRegionName() {

        if (regionCode == null) return "기타";

        return switch (regionCode) {
            case "1"  -> "서울";
            case "2"  -> "인천";
            case "3"  -> "대전";
            case "4"  -> "대구";
            case "5"  -> "광주";
            case "6"  -> "부산";
            case "7"  -> "울산";
            case "8"  -> "세종";
            case "31" -> "경기";
            case "32" -> "강원";
            case "33" -> "충북";
            case "34" -> "충남";
            case "35" -> "경북";
            case "36" -> "경남";
            case "37" -> "전북";
            case "38" -> "전남";
            case "39" -> "제주";
            default   -> "기타";
        };
    }
}
