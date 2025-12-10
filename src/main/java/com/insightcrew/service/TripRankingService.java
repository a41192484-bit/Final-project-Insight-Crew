package com.insightcrew.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.domain.trip.vo.RegionVo;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.domain.weather.vo.WeatherTodayVo;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRankingCacheMapper;
import com.insightcrew.repository.TripRegionMapper;
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
    private final TripRegionMapper regionMapper;

    /** 매일 05시 캐시용 TOP5 생성 */
    @Scheduled(cron = "0 0 5 * * *")
    public void updateDailyRanking() {

        List<TripVo> trips = tripMapper.findAll();
        if (trips.isEmpty()) return;

        rankingWeatherService.updateAllRegionsWeather();

        List<TripRankingDto> top5 = trips.stream()
                .map(this::createRanking)
                .sorted(Comparator.comparingInt(TripRankingDto::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        rankingMapper.deleteAll();

        int rank = 1;
        for (TripRankingDto dto : top5) {
            dto.setRankNo(rank++);
            rankingMapper.insertRanking(dto);
        }
    }

    /** 전체용 기본 랭킹 계산 */
    private TripRankingDto createRanking(TripVo trip) {

        TripRankingDto dto = new TripRankingDto();
        dto.setTripId(trip.getTripId());
        dto.setTitle(trip.getName());
        dto.setImageUrl(trip.getImageUrl());

        if (trip.getRegionId() == null) {
            dto.setScore(0);
            dto.setWeather("정보 없음");
            dto.setRegion("기타");
            dto.setReason("지역 정보 없음");
            return dto;
        }

        RegionVo region = regionMapper.findById(trip.getRegionId());
        dto.setRegion(region != null ? region.getSido() : "기타");

        WeatherTodayVo weather = weatherMapper.findByRegionId(trip.getRegionId());
        if (weather == null) {
            dto.setScore(0);
            dto.setWeather("정보 없음");
            dto.setReason("날씨 정보 없음");
            return dto;
        }

        int score = weatherScoreService.calculateScore(weather);

        dto.setScore(score);
        dto.setTemperature(weather.getTemp());
        dto.setWeather(makeWeatherText(weather));
        dto.setReason(makeReason(score));
        return dto;
    }


    // ======================================================
    // ⭐⭐⭐ 지역 기반 랭킹 생성 API ⭐⭐⭐
    // ======================================================
    public List<TripRankingDto> getRankingByRegion(String sido, String sigungu) {

        // 1) 시도+시군구 → region_id 조회
        RegionVo region = regionMapper.findBySidoAndSigungu(sido, sigungu);
        if (region == null) {
            System.out.println("❌ 지역 없음");
            return Collections.emptyList();
        }

        int regionId = region.getId();

        // 2) 해당 지역 날씨 조회
        WeatherTodayVo weather = weatherMapper.findByRegionId(regionId);
        if (weather == null) {
            System.out.println("❌ 날씨 정보 없음");
            return Collections.emptyList();
        }

        // 3) regionId 여행지들만 조회
        List<TripVo> trips = tripMapper.findByRegionId(regionId);
        if (trips.isEmpty()) {
            System.out.println("❌ 해당 지역 여행지 없음");
            return Collections.emptyList();
        }

        // 4) 지역 날씨 기반으로 점수 계산하여 랭킹 생성
        return trips.stream()
                .map(t -> createRankingForRegion(t, weather, region)) 
                .sorted(Comparator.comparingInt(TripRankingDto::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /** 지역 날씨 기반 랭킹 DTO 생성 */
    private TripRankingDto createRankingForRegion(TripVo trip, WeatherTodayVo regionWeather, RegionVo region) {

        TripRankingDto dto = new TripRankingDto();

        dto.setTripId(trip.getTripId());
        dto.setTitle(trip.getName());
        dto.setImageUrl(trip.getImageUrl());
        dto.setRegion(region.getSido());

        int score = weatherScoreService.calculateScore(regionWeather);
        dto.setScore(score);
        dto.setTemperature(regionWeather.getTemp());
        dto.setWeather(makeWeatherText(regionWeather));
        dto.setReason(makeReason(score));

        return dto;
    }

    // 공통 util 메서드
    private String makeWeatherText(WeatherTodayVo w) {
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

    private String makeReason(int score) {
        if (score >= 80) return "☀ 최고의 날씨! 지금 바로 떠나보세요.";
        if (score >= 60) return "🌤 여행하기 좋은 날씨예요.";
        if (score >= 40) return "⛅ 무난한 날씨, 가볍게 나들이 어떠세요?";
        if (score >= 20) return "🌥 조금 흐리지만 나쁘진 않아요.";
        return "☔ 비가 와요! 실내 여행지를 추천합니다.";
    }

    public List<TripRankingDto> getTodayRanking() {
        return rankingMapper.findTop5();
    }
}
