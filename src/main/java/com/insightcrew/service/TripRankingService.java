package com.insightcrew.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.domain.weather.vo.WeatherTodayVo;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRankingCacheMapper;
import com.insightcrew.repository.TripWeatherTodayMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripRankingService {

    private final TripMapper tripMapper;
    private final RankingWeatherService rankingWeatherService;
    private final WeatherScoreService weatherScoreService;

    private final TripWeatherTodayMapper weatherMapper;
    private final TripRankingCacheMapper rankingMapper;

    // ================================================================
    // 1) 매일 05시: 전국 여행지 TOP5 캐싱
    // ================================================================
    @Scheduled(cron = "0 0 5 * * *")
    public void updateDailyRanking() {

        System.out.println("⏳ 전국 여행지 랭킹 업데이트 시작...");

        // A. 전체 날씨 업데이트
        rankingWeatherService.updateAllRegionsWeather();

        // B. 전국 TOP5 계산
        List<TripRankingDto> top5 = getTop5();
        if (top5 == null || top5.isEmpty()) {
            System.out.println("❌ TOP5 생성 실패: 여행지 또는 날씨 없음");
            return;
        }

        // C. 캐시 초기화 후 저장
        rankingMapper.deleteAll();

        int rank = 1;
        for (TripRankingDto r : top5) {
            r.setRankNo(rank++);
            rankingMapper.insertRanking(r);
        }

        System.out.println("🌟 전국 TOP5 캐싱 완료");
    }

    // ================================================================
    // 2) 전국 여행지 기반 TOP5
    // ================================================================
    public List<TripRankingDto> getTop5() {

        List<TripVo> trips = tripMapper.findAll();
        if (trips == null || trips.isEmpty()) {
            System.out.println("❌ 여행지 데이터 없음");
            return List.of();
        }

        // ★ regionId 없는 데이터 제외하기 (날씨 매핑 불가)
        trips = trips.stream()
                .filter(t -> t.getRegionId() != null)
                .collect(Collectors.toList());

        if (trips.isEmpty()) {
            System.out.println("❌ regionId 매핑된 여행지가 없음");
            return List.of();
        }

        // 날씨 기반 점수 계산 → TOP5
        return trips.stream()
                .map(this::createRankingForTrip)
                .filter(dto -> dto.getScore() >= 0) // 정상만
                .sorted(Comparator.comparingInt(TripRankingDto::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // ================================================================
    // 3) 개별 여행지 DTO 생성
    // ================================================================
    private TripRankingDto createRankingForTrip(TripVo trip) {

        Integer regionId = trip.getRegionId();
        if (regionId == null) {
            return emptyRanking(trip);
        }

        WeatherTodayVo weather = weatherMapper.findByRegionId(regionId);
        if (weather == null) {
            return emptyRanking(trip);
        }

        int score = weatherScoreService.calculateScore(weather);

        TripRankingDto dto = new TripRankingDto();
        dto.setTripId(trip.getTripId());
        dto.setTitle(trip.getName());
        dto.setImageUrl(trip.getImageUrl());
        dto.setRegion(trip.getSido() + " " + trip.getSigungu());
        dto.setScore(score);
        dto.setTemperature(weather.getTemp());
        dto.setWeather(toWeatherText(weather));
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
        dto.setScore(-1); // 점수 없음 = 필터 처리용
        return dto;
    }

    // ================================================================
    // 4) 공통 변환 메서드들
    // ================================================================
    private String toWeatherText(WeatherTodayVo w) {

        if (w.getPty() != null && w.getPty() != 0) {
            return switch (w.getPty()) {
                case 1 -> "비";
                case 2 -> "비/눈";
                case 3 -> "눈";
                case 4 -> "소나기";
                default -> "강수";
            };
        }

        return switch (w.getSky() != null ? w.getSky() : 0) {
            case 1 -> "맑음";
            case 3 -> "구름많음";
            case 4 -> "흐림";
            default -> "날씨 정보 없음";
        };
    }

    private String toReason(int score) {
        if (score >= 80) return "☀ 최고의 날씨! 지금 바로 떠나보세요.";
        if (score >= 60) return "🌤 여행하기 좋은 날씨예요.";
        if (score >= 40) return "⛅ 무난한 날씨, 가볍게 나들이 어떠세요?";
        if (score >= 20) return "🌥 조금 흐리지만 나쁘진 않아요.";
        return "☔ 비가 와요! 실내 여행지를 추천합니다.";
    }

    // ================================================================
    // 5) 캐시 읽기 (메인 fallback)
    // ================================================================
    public List<TripRankingDto> getTodayRanking() {
        return rankingMapper.findTop5();
    }
}
