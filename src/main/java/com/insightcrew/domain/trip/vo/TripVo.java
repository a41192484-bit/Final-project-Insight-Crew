package com.insightcrew.domain.trip.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripVo {

	private Long tripId; // 여행지 고유번호

	private String contentId;       // 관광공사 여행지 고유 아이디 (contentid)
	private String name;            // 여행지명 (title)
	private String category;        // 카테고리, ENUM이 DB에는 String으로 저장됨
	private String fullAddress;     // 주소 addr1+addr2
	private String imageUrl;        // 메인 이미지 url
	private String detailImageUrl;  // 상세보기 이미지 url

	private Double lat;             // 위도(mapy)
	private Double lon;             // 경도(mapx)

	private String regionCode;      // 지역 코드 (areacode)
	private String contentTypeId;   // 관광공사 유형코드 (contentTypeId)

	private String tel;             // 전화번호 (tel)
	private String homepage;        // 홈페이지 주소(detailCommon2 -> homepage)
	
	private String description;     // 설명(overview)
	
	public String getImageUrl() {
	    if (imageUrl == null || imageUrl.trim().isEmpty()) {
	        return "/images/no-image.png";
	    }
	    return imageUrl;
	}
}
