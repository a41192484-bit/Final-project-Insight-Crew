// 임시 클래스
package com.insightcrew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.service.RankingWeatherService;
import com.insightcrew.service.TripRankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripAdminController {

    private final RankingWeatherService rankingWeatherService;
    private final TripRankingService tripRankingService;

    /** 전국 날씨 갱신 */
    @GetMapping("/admin/update-weather")
    public String updateWeather() {
        rankingWeatherService.updateAllRegionsWeather();
        return "✅ Weather updated!";
    }

    /** 랭킹 재생성 */
    @GetMapping("/admin/update-ranking")
    public String updateRanking() {
        tripRankingService.updateDailyRanking();
        return "✅ Ranking updated!";
    }

    /** 한 번에 둘 다 실행 */
    @GetMapping("/admin/update-all")
    public String updateAll() {
        rankingWeatherService.updateAllRegionsWeather();
        tripRankingService.updateDailyRanking();
        return "🔥 All updated!";
    }
}
