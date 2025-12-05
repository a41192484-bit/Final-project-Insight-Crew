package com.insightcrew.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WeatherDateUtil {

    public static String getBaseDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getBaseTime() {
        return "0500";
    }
}
