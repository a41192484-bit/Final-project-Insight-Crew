package com.insightcrew.api;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insightcrew.domain.trip.dto.TripApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TripApiClient {

    private final RestTemplate restTemplate;
    // 간단하게 내부에서 쓸 ObjectMapper 하나만 사용
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASE_URL =
            "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";

    private static final String SERVICE_KEY =
            "78b9c2a83c39592f5ff7d8d2894fb9dcda394689982f0a4434c07b3c13af9f1c";

    /**
     * contentTypeId, areaCode, pageNo 기준으로 관광공사 API 호출
     * - 호출 실패 시 null 반환
     * - items 구조가 이상한 경우 최대한 보정 후 파싱
     */
    public TripApiResponse search(String contentTypeId, int areaCode, int pageNo) {

        String url = new StringBuilder(BASE_URL)
                .append("?serviceKey=").append(SERVICE_KEY)
                .append("&numOfRows=20")
                .append("&pageNo=").append(pageNo)
                .append("&MobileOS=ETC")
                .append("&MobileApp=TripInsight")
                .append("&_type=json")
                .append("&contentTypeId=").append(contentTypeId)
                .append("&areaCode=").append(areaCode)
                .toString();

        String json;

        try {
            json = restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.out.println("⚠️ [TripApiClient] API 호출 실패 → url=" + url);
            return null;
        }

        if (json == null || json.isBlank()) {
            System.out.println("⚠ [TripApiClient] 빈 응답 → url=" + url);
            return null;
        }

        // 관광공사 API가 items를 이상하게 내려줄 때 방어
        json = normalizeItems(json);

        try {
            return objectMapper.readValue(json, TripApiResponse.class);
        } catch (Exception e) {
            System.out.println("⚠️ [TripApiClient] JSON 파싱 에러 → pageNo=" + pageNo);
            // e.printStackTrace(); 필요하면 추가
            return null;
        }
    }

    /**
     * items 관련 이상한 형태들을 통일
     *  - "items":""      → "items":{"item":[]}
     *  - "items":{}      → "items":{"item":[]}
     *  - "items":null    → "items":{"item":[]}
     *  - "item":""       → "item":[]
     */
    private String normalizeItems(String json) {
        return json
                .replace("\"items\":\"\"", "\"items\":{\"item\":[]}")
                .replace("\"items\":{}", "\"items\":{\"item\":[]}")
                .replace("\"items\":null", "\"items\":{\"item\":[]}")
                .replace("\"item\":\"\"", "\"item\":[]");
    }
}