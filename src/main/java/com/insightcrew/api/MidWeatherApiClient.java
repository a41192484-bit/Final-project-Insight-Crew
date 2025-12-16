package com.insightcrew.api;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.insightcrew.domain.weather.dto.MidLandForecastItemDto;
import com.insightcrew.domain.weather.dto.MidLandForecastResponse;
import com.insightcrew.domain.weather.dto.MidTempForecastItemDto;
import com.insightcrew.domain.weather.dto.MidTempForecastResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MidWeatherApiClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String serviceKey;

    private static final String LAND_URL =
        "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";

    private static final String TEMP_URL =
        "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";

    /* =========================
     * 중기 육상예보
     * ========================= */
    public MidLandForecastItemDto getLandForecast(String regId, String tmFc) {

        URI uri = URI.create(LAND_URL
                + "?serviceKey=" + encode(serviceKey)
                + "&dataType=JSON"
                + "&regId=" + regId
                + "&tmFc=" + tmFc
        );

        MidLandForecastResponse response =
                restTemplate.getForObject(uri, MidLandForecastResponse.class);

        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null
                || response.getResponse().getBody().getItems().getItem().isEmpty()) {
            System.out.println("❌ 중기 육상예보 응답 비정상");
            return null;
        }

        MidLandForecastItemDto item =
                response.getResponse()
                        .getBody()
                        .getItems()
                        .getItem()
                        .get(0);

        System.out.println("✅ 중기 육상예보 DTO = " + item);

        return item;
    }

    /* =========================
     * 중기 기온예보
     * ========================= */
    public MidTempForecastItemDto getTempForecast(String regId, String tmFc) {

        URI uri = URI.create(TEMP_URL
                + "?serviceKey=" + encode(serviceKey)
                + "&dataType=JSON"
                + "&regId=" + regId
                + "&tmFc=" + tmFc
        );

        // 🔥 1️⃣ RAW JSON 확인
        String rawJson = restTemplate.getForObject(uri, String.class);
        System.out.println("🌡 중기 기온 RAW JSON = " + rawJson);

        MidTempForecastResponse response =
                restTemplate.getForObject(uri, MidTempForecastResponse.class);

        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null
                || response.getResponse().getBody().getItems().getItem().isEmpty()) {
            System.out.println("❌ 중기 기온예보 응답 비정상");
            return null;
        }

        MidTempForecastItemDto item =
                response.getResponse()
                        .getBody()
                        .getItems()
                        .getItem()
                        .get(0);

        // 🔥 2️⃣ DTO 값 확인
        System.out.println("✅ 중기 기온 DTO = " + item);

        return item;
    }

    /* =========================
     * 공통 유틸
     * ========================= */
    private String encode(String key) {
        return URLEncoder.encode(key, StandardCharsets.UTF_8);
    }
}
