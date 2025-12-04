package com.insightcrew.domain.trip.dto;

import lombok.Data;

@Data
public class TripDetailResponse {
	private Long tripId;
	private String name;
	private String fullAddress;
	private String description;

	private String imageUrl;
	private String detailImageUrl;

	private String tel;
	private String homepage;
	private String category;

	private Double lat;
	private Double lon;
}
