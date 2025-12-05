package com.insightcrew.api;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.insightcrew.domain.weather.dto.WeatherApiResponse;
import com.insightcrew.domain.weather.dto.WeatherItemDto;
import com.insightcrew.util.WeatherDateUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TripApiWeatherClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String serviceKey;

    private static final String URL =
            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    // ===========================
    //  날씨 조회 (nx, ny)
    // ===========================
    public List<WeatherItemDto> getWeather(int nx, int ny) {

        String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = getBaseTime(); // 기상청 발표 시간 로직 적용

        // 인증키는 반드시 URL 인코딩 적용
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
            System.out.println("⚠️ [Weather API Error] 호출 실패 → " + uri);
            return Collections.emptyList();
        }

        if (response == null ||
                response.getResponse() == null ||
                response.getResponse().getBody() == null ||
                response.getResponse().getBody().getItems() == null ||
                response.getResponse().getBody().getItems().getItem() == null
        ) {
            System.out.println("⚠️ [Weather API Warning] 빈 데이터 반환됨");
            return Collections.emptyList();
        }

        return response.getResponse().getBody().getItems().getItem();
    }

    // ===========================
    //  기상청 단기예보 발표시간 계산
    // ===========================
    private String getBaseTime() {

        LocalTime now = LocalTime.now();

        // 발표시간은 매일 02, 05, 08, 11, 14, 17, 20, 23시
        // 각 발표는 10분 이후부터 조회 가능

        if (now.isBefore(LocalTime.of(2, 10)))  return "2300";
        if (now.isBefore(LocalTime.of(5, 10)))  return "0200";
        if (now.isBefore(LocalTime.of(8, 10)))  return "0500";
        if (now.isBefore(LocalTime.of(11, 10))) return "0800";
        if (now.isBefore(LocalTime.of(14, 10))) return "1100";
        if (now.isBefore(LocalTime.of(17, 10))) return "1400";
        if (now.isBefore(LocalTime.of(20, 10))) return "1700";
        if (now.isBefore(LocalTime.of(23, 10))) return "2000";

        return "2300";
    }
    
}