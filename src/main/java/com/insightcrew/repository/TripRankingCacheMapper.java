package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.insightcrew.domain.trip.dto.TripRankingDto;

@Mapper
public interface TripRankingCacheMapper {

    void deleteAll();                     // 기존 랭킹 싹 지우기

    void insertRanking(TripRankingDto dto); // 랭킹 1줄 저장

    List<TripRankingDto> findTop5();      // 화면에서 조회용

}
