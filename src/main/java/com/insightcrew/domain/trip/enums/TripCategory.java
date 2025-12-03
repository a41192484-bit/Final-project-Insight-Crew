package com.insightcrew.domain.trip.enums;

public enum TripCategory {
	NATURE("자연"), CULTURE("문화/역사"), LEISURE("체험/레저"), FOOD("음식"), SHOPPING("쇼핑");

	private final String displayName;

	TripCategory(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static TripCategory fromApi(String contentTypeId, String cat1) {

		if (contentTypeId == null)
			return NATURE; // 안전 fallback
		if (cat1 == null)
			cat1 = ""; // NPE 방어

		// 자연
		if ("12".equals(contentTypeId) && "A01".equals(cat1)) {
			return NATURE;
		}

		// 문화/역사
		if ("12".equals(contentTypeId) && "A02".equals(cat1)) {
			return CULTURE;
		}
		if ("14".equals(contentTypeId)) {
			return CULTURE;
		}

		// 체험/레저
		if ("28".equals(contentTypeId) || "15".equals(contentTypeId)) {
			return LEISURE;
		}

		// 음식
		if ("39".equals(contentTypeId)) {
			return FOOD;
		}

		// 쇼핑
		if ("38".equals(contentTypeId)) {
			return SHOPPING;
		}

		// 기본값은 자연
		return NATURE;
	}
}
