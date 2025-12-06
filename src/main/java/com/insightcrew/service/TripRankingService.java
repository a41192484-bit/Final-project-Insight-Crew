package com.insightcrew.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.domain.weather.dto.WeatherItemDto;
import com.insightcrew.domain.weather.vo.WeatherTodayVo;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRankingCacheMapper;
import com.insightcrew.repository.TripWeatherTodayMapper;
import com.insightcrew.util.GridConverter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripRankingService {

    private final TripMapper tripMapper;
    private final TripWeatherScoreService scoreService;
    private final TripRankingCacheMapper rankingCacheMapper;

    private final TripWeatherTodayMapper weatherTodayMapper;
    private final TripWeatherCacheService weatherCacheService;

    /**
     * 매일 05:00 TOP5 랭킹 생성
     */
    // @Scheduled(cron = "0 0 5 * * *")
    @Scheduled(cron = "0 43 12 * * *")
    public void updateDailyRanking() {

        System.out.println("🔄 [랭킹 업데이트 시작] 여행지 추천 생성 중...");

        // 1) 전체 여행지 조회
        List<TripVo> trips = tripMapper.findAll();

        // 2) 각 여행지의 점수 계산
        List<TripRankingDto> scoredList = trips.stream()
                .map(this::createRankingDto)
                .collect(Collectors.toList());

        // 3) 점수 기준 TOP5 추출
        List<TripRankingDto> top5 = scoredList.stream()
                .sorted(Comparator.comparingInt(TripRankingDto::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // 4) 캐시 초기화
        rankingCacheMapper.deleteAll();

        // 5) TOP5 DB 저장
        int rank = 1;
        for (TripRankingDto dto : top5) {
            dto.setRankNo(rank++);
            rankingCacheMapper.insertRanking(dto);
        }

        System.out.println("✅ [랭킹 업데이트 완료] TOP5 생성 성공!");
    }

    /**
     * TripRankingDto 생성 (날씨 기반 점수 계산 포함)
     */
    private TripRankingDto createRankingDto(TripVo trip) {

        TripRankingDto dto = new TripRankingDto();
        dto.setTripId(trip.getTripId());
        dto.setTitle(trip.getName());
        dto.setImageUrl(trip.getImageUrl());

        // ⭐ DB에 저장할 지역 정보
        dto.setRegion(convertRegionCode(trip.getRegionCode()));

        // 좌표 없으면 점수 0 처리
        if (trip.getLat() == null || trip.getLon() == null) {
            dto.setScore(0);
            dto.setReason("좌표 정보 없음");
            return dto;
        }

        // 위경도 → 기상청 격자
        int[] grid = GridConverter.toGrid(trip.getLat(), trip.getLon());
        String regionKey = grid[0] + "-" + grid[1];

        // today-weather 캐시 조회
        WeatherTodayVo cached = weatherTodayMapper.findByRegion(regionKey);

        WeatherTodayVo weatherVo = (cached != null)
                ? cached
                : weatherCacheService.updateTodayWeather(trip.getLat(), trip.getLon());

        // WeatherVo → List<WeatherItemDto>
        List<WeatherItemDto> weatherItems =
                weatherCacheService.convertToWeatherItems(weatherVo);

        int score = scoreService.calculateScore(weatherItems);
        dto.setScore(score);

        dto.setTemperature(weatherVo.getTemp());
        dto.setWeather(convertWeatherText(weatherVo.getSky(), weatherVo.getPty()));

        dto.setReason(makeReason(score));

        return dto;
    }

    /**
     * 전국 TOP5 조회
     */
    public List<TripRankingDto> getTodayRanking() {
        return rankingCacheMapper.findTop5();
    }

    /**
     * 지역별 TOP5 조회
     */
    public List<TripRankingDto> getTodayRankingByRegion(String region) {
        return rankingCacheMapper.findTop5ByRegion(region);
    }

    /**
     * 추천 이유 생성
     */
    private String makeReason(int score) {
        if (score >= 80) return "☀ 최고의 날씨! 지금 바로 떠나보세요.";
        if (score >= 60) return "🌤 여행하기 좋은 날씨예요.";
        if (score >= 40) return "⛅ 무난한 날씨, 가볍게 나들이 어떠세요?";
        if (score >= 20) return "🌥 조금 흐리지만 나쁘진 않아요.";
        return "☔ 비가 와요! 실내 여행지를 추천합니다.";
    }

    /**
     * SKY + PTY → 날씨 텍스트 변환
     */
    private String convertWeatherText(Integer sky, Integer pty) {

        if (pty != null && pty != 0) {
            return switch (pty) {
                case 1 -> "비";
                case 2 -> "비/눈";
                case 3 -> "눈";
                case 4 -> "소나기";
                default -> "강수";
            };
        }

        return switch (sky != null ? sky : 0) {
            case 1 -> "맑음";
            case 3 -> "구름많음";
            case 4 -> "흐림";
            default -> "날씨 정보 없음";
        };
    }

    /**
     * 관광공사 지역코드 → 시/도명 변환
     */
    private String convertRegionCode(String code) {

        if (code == null) return "기타";

        return switch (code) {
            case "1"  -> "서울";
            case "2"  -> "인천";
            case "3"  -> "대전";
            case "4"  -> "대구";
            case "5"  -> "광주";
            case "6"  -> "부산";
            case "7"  -> "울산";
            case "8"  -> "세종";
            case "31" -> "경기";
            case "32" -> "강원";
            case "33" -> "충북";
            case "34" -> "충남";
            case "35" -> "경북";
            case "36" -> "경남";
            case "37" -> "전북";
            case "38" -> "전남";
            case "39" -> "제주";
            default   -> "기타";
        };
    }
}