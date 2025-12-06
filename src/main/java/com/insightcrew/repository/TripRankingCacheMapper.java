package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.trip.dto.TripRankingDto;

@Mapper
public interface TripRankingCacheMapper {

    void deleteAll();

    void insertRanking(TripRankingDto dto);

    List<TripRankingDto> findTop5();

    List<TripRankingDto> findTop5ByRegion(@Param("region") String region);
}
