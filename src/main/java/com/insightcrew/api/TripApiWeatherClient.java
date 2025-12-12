package com.insightcrew.api;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.insightcrew.domain.weather.dto.WeatherApiResponse;
import com.insightcrew.domain.weather.dto.WeatherItemDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TripApiWeatherClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String serviceKey;

    /** ⭐ 단기예보 API (우리 DTO와 100% 호환) */
    private static final String URL =
            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    /** ===========================
     *   지역 날씨 조회
     *  =========================== */
    public List<WeatherItemDto> getWeather(int nx, int ny) {

        String baseDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String baseTime = getBaseTimeForFcst();  // 예보 발표시간 계산

        return callWeatherApi(baseDate, baseTime, nx, ny);
    }

    /** ===========================
     *   단기예보 발표시간 계산
     *  =========================== */
    private String getBaseTimeForFcst() {

        LocalTime now = LocalTime.now();

        // 발표시각: 02, 05, 08, 11, 14, 17, 20, 23
        int[][] times = {
                {2,10}, {5,10}, {8,10}, {11,10},
                {14,10}, {17,10}, {20,10}, {23,10}
        };
        String[] baseTimes = {"2300","0200","0500","0800","1100","1400","1700","2000"};

        for (int i = 0; i < times.length; i++) {
            if (now.isBefore(LocalTime.of(times[i][0], times[i][1]))) {
                return baseTimes[i];
            }
        }

        return "2300";
    }

    /** ===========================
     *   API 공통 호출부
     *  =========================== */
    private List<WeatherItemDto> callWeatherApi(String baseDate, String baseTime, int nx, int ny) {

        String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder(URL)
                .append("?serviceKey=").append(encodedKey)
                .append("&numOfRows=300")
                .append("&pageNo=1")
                .append("&dataType=JSON")
                .append("&base_date=").append(baseDate)
                .append("&base_time=").append(baseTime)
                .append("&nx=").append(nx)
                .append("&ny=").append(ny);

        URI uri = URI.create(sb.toString());

        WeatherApiResponse response;
        try {
            response = restTemplate.getForObject(uri, WeatherApiResponse.class);
        } catch (Exception e) {
            System.out.println("⚠️ [Weather API Error] → " + uri);
            return Collections.emptyList();
        }

        if (response == null ||
                response.getResponse() == null ||
                response.getResponse().getBody() == null ||
                response.getResponse().getBody().getItems() == null ||
                response.getResponse().getBody().getItems().getItem() == null
        ) {
            System.out.println("⚠️ [Weather API] 빈 데이터 반환");
            return Collections.emptyList();
        }

        return response.getResponse().getBody().getItems().getItem();
    }
}
