package com.insightcrew.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.insightcrew.api.TripApiWeatherClient;
import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.domain.weather.dto.WeatherItemDto;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRankingCacheMapper;
import com.insightcrew.util.GridConverter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripRankingService {

    private final TripMapper tripMapper;
    private final TripApiWeatherClient weatherClient;
    private final TripWeatherScoreService scoreService;
    private final TripRankingCacheMapper rankingCacheMapper;

    /** ============================
     *   매일 05:00 자동 랭킹 업데이트
     * ============================ */
    @Scheduled(cron = "0 0 5 * * *")
    public void updateDailyRanking() {

        System.out.println("🔄 [랭킹 업데이트 시작] 여행지 추천 생성 중...");

        // 1) 전체 여행지 조회
        List<TripVo> trips = tripMapper.findAll();

        // 2) 날씨 기반 점수 계산
        List<TripRankingDto> scoredList = trips.stream().map(trip -> {

            int[] grid = GridConverter.toGrid(trip.getLat(), trip.getLon());

            List<WeatherItemDto> weather = weatherClient.getWeather(grid[0], grid[1]);

            // weather API 에러 방어
            if (weather == null) {
                weather = List.of();
            }

            int score = scoreService.calculateScore(weather);

            TripRankingDto dto = new TripRankingDto();
            dto.setTripId(trip.getTripId());
            dto.setTitle(trip.getName());
            dto.setImageUrl(trip.getImageUrl());
            dto.setScore(score);
            dto.setReason(makeReason(score));

            return dto;

        }).collect(Collectors.toList());

        // 3) 점수 기준 정렬 후 TOP5 선정
        List<TripRankingDto> top5 = scoredList.stream()
                .sorted(Comparator.comparingInt(TripRankingDto::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // 4) 기존 캐시 삭제
        rankingCacheMapper.deleteAll();

        // 5) 새로운 랭킹 저장
        int rank = 1;
        for (TripRankingDto dto : top5) {
            dto.setRankNo(rank++);
            rankingCacheMapper.insertRanking(dto);
        }

        System.out.println("✅ [랭킹 업데이트 완료] TOP5 생성 성공!");
    }

    /** 화면에서 오늘의 TOP5 조회 */
    public List<TripRankingDto> getTodayRanking() {
        return rankingCacheMapper.findTop5();
    }

    /** 추천 이유 생성 */
    private String makeReason(int score) {

        if (score >= 80) return "☀ 최고의 날씨! 지금 바로 떠나보세요.";
        if (score >= 60) return "🌤 여행하기 좋은 날씨예요.";
        if (score >= 40) return "⛅ 무난한 날씨, 가볍게 나들이 어떠세요?";
        if (score >= 20) return "🌥 조금 흐리지만 나쁘진 않아요.";
        return "☔ 비가 와요! 실내 여행지를 추천합니다.";
    }
}
