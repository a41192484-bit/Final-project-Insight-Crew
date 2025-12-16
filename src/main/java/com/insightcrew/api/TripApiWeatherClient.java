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

    /** ⭐ 단기예보 API */
    private static final String URL =
            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    /* =====================================================
     *  단기예보 조회 (오늘 + 내일 + 모레)
     *  ✔ TMX / TMN 안정 확보용
     * ===================================================== */
    public List<WeatherItemDto> getWeather(int nx, int ny) {

        LocalDate baseDate = LocalDate.now();

        // 기상청 단기예보 기준시각 계산
        String baseTime = getBaseTimeForFcst();

        // 새벽 + 2300이면 날짜를 하루 빼야 함
        if ("2300".equals(baseTime)
                && LocalTime.now().isBefore(LocalTime.of(2, 10))) {
            baseDate = baseDate.minusDays(1);
        }
        
        List<WeatherItemDto> items = callWeatherApi(
                baseDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                baseTime,
                nx,
                ny
        );
        
        return callWeatherApi(
                baseDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                baseTime,
                nx,
                ny
        );
        
        
    }
    
    /* =====================================================
     * 단기예보 base_time 계산 (절대 중요)
     * ===================================================== */
    private String getBaseTimeForFcst() {

        LocalTime now = LocalTime.now();

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

    /* =====================================================
     *  API 공통 호출부
     * ===================================================== */
    private List<WeatherItemDto> callWeatherApi(
            String baseDate,
            String baseTime,
            int nx,
            int ny
    ) {

        String encodedKey =
                URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

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
            response =
                    restTemplate.getForObject(uri, WeatherApiResponse.class);
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

        return response.getResponse()
                       .getBody()
                       .getItems()
                       .getItem();
    }
}
