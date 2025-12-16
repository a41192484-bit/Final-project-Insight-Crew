package com.insightcrew.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WeatherDateUtil {

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    /* =========================
     * 단기/초단기 예보용
     * ========================= */

    // 오늘 날짜 (yyyyMMdd)
    public static String getBaseDate() {
        return LocalDate.now().format(DATE);
    }

    /**
     * 단기예보 baseTime
     * - 기상청 단기예보 발표 시각 기준
     * - 02, 05, 08, 11, 14, 17, 20, 23
     */
    public static String getBaseTime() {
        int hour = LocalTime.now().getHour();

        if (hour < 2)  return "2300";
        if (hour < 5)  return "0200";
        if (hour < 8)  return "0500";
        if (hour < 11) return "0800";
        if (hour < 14) return "1100";
        if (hour < 17) return "1400";
        if (hour < 20) return "1700";
        if (hour < 23) return "2000";
        return "2300";
    }

    /* =========================
     * 중기예보 발표 시각용
     * ========================= */

    /**
     * 중기예보 tmFc
     * - 하루 2회: 06:00 / 18:00
     */
    public static String getMidForecastTime() {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 00:00 ~ 05:59 → 전날 18시
        if (now.isBefore(LocalTime.of(6, 0))) {
            return today.minusDays(1).format(DATE) + "1800";
        }

        // 06:00 ~ 17:59 → 오늘 06시
        if (now.isBefore(LocalTime.of(18, 0))) {
            return today.format(DATE) + "0600";
        }

        // 18:00 이후 → 오늘 18시
        return today.format(DATE) + "1800";
    }
    
    public static LocalDate getMidForecastBaseDate() {
        // tmFc 기준 날짜 (yyyyMMddHHmm → yyyyMMdd)
        String tmFc = getMidForecastTime(); // 이미 쓰고 있는 메서드
        String datePart = tmFc.substring(0, 8);

        return LocalDate.parse(datePart, DateTimeFormatter.BASIC_ISO_DATE);
    }
    
}
