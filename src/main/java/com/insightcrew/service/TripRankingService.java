package com.insightcrew.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.domain.weather.vo.WeatherForecastVo;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRankingCacheMapper;
import com.insightcrew.repository.WeatherForecastMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripRankingService {

    private final TripMapper tripMapper;
    private final WeatherScoreService weatherScoreService;

    private final WeatherForecastMapper forecastMapper;
    private final TripRankingCacheMapper rankingMapper;

    // ================================================================
    // 1 매일 05시: 전국 여행지 TOP5 캐싱
    // ================================================================
    @Scheduled(cron = "0 0 5 * * *")
    public void updateDailyRanking() {

        System.out.println("⏳ 전국 여행지 랭킹 배치 시작");

        // ⚠️ 날씨 캐시는 WeatherForecastService에서 관리
        // 여기서는 캐시를 "신뢰하고" 랭킹만 계산한다

        List<TripRankingDto> top5 = calculateTop5();
        if (top5.isEmpty()) {
            System.out.println("❌ 랭킹 생성 실패");
            return;
        }

        rankingMapper.deleteAll();

        int rank = 1;
        for (TripRankingDto dto : top5) {
            dto.setRankNo(rank++);
            rankingMapper.insertRanking(dto);
        }

        System.out.println("🌟 전국 TOP5 캐시 저장 완료");
    }

    // ================================================================
    // 2 랭킹 계산
    // ================================================================
    private List<TripRankingDto> calculateTop5() {

        List<TripVo> trips = tripMapper.findAll();
        if (trips == null || trips.isEmpty()) {
            return List.of();
        }

        return trips.stream()
                .filter(t -> t.getRegionId() != null)
                .map(this::createRankingForTrip)
                .filter(dto -> dto.getScore() >= 0)
                .sorted(Comparator.comparingInt(TripRankingDto::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // ================================================================
    // 3 여행지별 랭킹 DTO 생성
    // ================================================================
    private TripRankingDto createRankingForTrip(TripVo trip) {

        WeatherForecastVo today =
            forecastMapper.findTodayByRegion(trip.getRegionId());

        if (today == null) {
            return emptyRanking(trip);
        }

        int score = weatherScoreService.calculateScore(today);

        TripRankingDto dto = new TripRankingDto();
        dto.setTripId(trip.getTripId());
        dto.setTitle(trip.getName());
        dto.setImageUrl(trip.getImageUrl());
        dto.setRegion(trip.getSido() + " " + trip.getSigungu());
        dto.setScore(score);

        if (today.getMinTemp() != null && today.getMaxTemp() != null) {
            dto.setTemperature(
                (today.getMinTemp() + today.getMaxTemp()) / 2.0
            );
        }

        dto.setWeather(toWeatherText(today));
        dto.setReason(toReason(score));

        return dto;
    }

    private TripRankingDto emptyRanking(TripVo trip) {

        TripRankingDto dto = new TripRankingDto();
        dto.setTripId(trip.getTripId());
        dto.setTitle(trip.getName());
        dto.setImageUrl(trip.getImageUrl());
        dto.setRegion(
            (trip.getSido() != null ? trip.getSido() : "") +
            (trip.getSigungu() != null ? " " + trip.getSigungu() : "")
        );
        dto.setScore(-1);
        return dto;
    }

    // ================================================================
    // 4 캐시 조회 (메인 화면)
    // ================================================================
    public List<TripRankingDto> getTodayRanking() {
        return rankingMapper.findTop5();
    }

    // ================================================================
    // 5 날씨 텍스트 변환
    // ================================================================
    private String toWeatherText(WeatherForecastVo w) {

        if (w.getRainPercent() != null && w.getRainPercent() >= 60) {
            return "비";
        }

        if (w.getCloudPercent() != null) {
            int cloud = w.getCloudPercent();
            if (cloud < 30) return "맑음";
            if (cloud < 60) return "구름 조금";
            if (cloud < 85) return "흐림";
        }

        return w.getWeatherText() != null
                ? w.getWeatherText()
                : "날씨 정보 없음";
    }

    private String toReason(int score) {
        if (score >= 80) return "☀ 최고의 날씨! 지금 바로 떠나보세요.";
        if (score >= 60) return "🌤 여행하기 좋은 날씨예요.";
        if (score >= 40) return "⛅ 무난한 날씨, 가볍게 나들이 어떠세요?";
        if (score >= 20) return "🌥 조금 흐리지만 나쁘진 않아요.";
        return "☔ 비 예보가 있어요. 실내 여행지를 추천합니다.";
    }
}
