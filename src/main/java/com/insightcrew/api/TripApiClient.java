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
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASE_URL =
            "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";

    private static final String SERVICE_KEY =
            "78b9c2a83c39592f5ff7d8d2894fb9dcda394689982f0a4434c07b3c13af9f1c";

    public TripApiResponse search(String contentTypeId, int areaCode, int pageNo) {

        StringBuilder url = new StringBuilder(BASE_URL)
                .append("?serviceKey=").append(SERVICE_KEY)
                .append("&numOfRows=20")
                .append("&pageNo=").append(pageNo)
                .append("&MobileOS=ETC")
                .append("&MobileApp=TripInsight")
                .append("&_type=json")
                .append("&contentTypeId=").append(contentTypeId)
                .append("&areaCode=").append(areaCode);

        String json = restTemplate.getForObject(url.toString(), String.class);

        // 관광공사 API가 items:"" 으로 내려줄 때 방어
        if (json.contains("\"items\":\"\"")) {
            json = json.replace("\"items\":\"\"", "\"items\":{\"item\":[]}");
        }

        try {
            return objectMapper.readValue(json, TripApiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("API parsing error", e);
        }
    }
}
