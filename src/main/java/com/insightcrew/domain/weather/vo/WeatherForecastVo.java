package com.insightcrew.domain.weather.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WeatherForecastVo {

    // PK는 캐시 테이블이므로 굳이 안 써도 됨
    private Integer regionId;

    /** 날짜 */
    private LocalDate weatherDate;

    /** 기온 */
    private Integer minTemp;
    private Integer maxTemp;

    /** 강수 확률 (%) */
    private Integer rainPercent;

    /** 바람 (m/s) */
    private Integer windSpeed;

    /** 구름량 (%) */
    private Integer cloudPercent;

    /** 날씨 설명 */
    private String weatherText;

    /** OpenWeather 아이콘 코드 */
    private String iconCode;

    /** 캐시 갱신 시간 */
    private LocalDateTime updatedAt;
    
    /** Icon Url **/
    public String getIconUrl() {
        if (iconCode == null || iconCode.isBlank()) {
            return "/img/weather/default.png"; // 없어도 되면 null 리턴해도 됨
        }
        return "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
    }
}
