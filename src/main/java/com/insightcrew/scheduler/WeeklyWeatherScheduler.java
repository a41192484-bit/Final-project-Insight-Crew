package com.insightcrew.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.service.WeatherForecastService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeeklyWeatherScheduler {

    private final TripRegionMapper regionMapper;
    private final WeatherForecastService weatherForecastService;

    /**
     * 매일 새벽 05:00
     * 전 지역 주간 날씨 갱신
     */
    @Scheduled(cron = "0 0 5 * * *")
    public void updateWeeklyWeatherAllRegions() {
        regionMapper.findAll().forEach(region -> {
            try {
                weatherForecastService.forceUpdate(
                    region.getId(),
                    region.getLat(),
                    region.getLon()
                );
            } catch (Exception e) {
                System.out.println("날씨 수집 실패 regionId=" + region.getId());
            }
        });
    }
    
    
      // 개발용,, 호출제한 유의
//    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
//    public void devUpdateAllRegions() {
//
//        regionMapper.findAll().forEach(region -> {
//            try {
//                weatherForecastService.forceUpdate(
//                    region.getId(),
//                    region.getLat(),
//                    region.getLon()
//                );
//            } catch (Exception e) {
//                System.out.println("[DEV] 날씨 수집 실패 regionId=" + region.getId());
//            }
//        });
//    }

}
