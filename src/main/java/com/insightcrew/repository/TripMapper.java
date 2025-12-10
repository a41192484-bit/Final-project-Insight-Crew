package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.trip.dto.TripDetailResponse;
import com.insightcrew.domain.trip.vo.TripVo;

@Mapper
public interface TripMapper {

    // 기존
    int existsByContentId(String contentId);
    void insert(TripVo tripVo);
    List<TripVo> findAll();
    TripVo findById(Long tripId);
    List<TripVo> findPage(@Param("offset") int offset, @Param("size") int size);
    int countAll();
    TripDetailResponse findTripDetailById(Long tripId);

    // 신규 - 검색 + 카테고리 + 페이징
    List<TripVo> searchTrips(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    int countTrips(
            @Param("keyword") String keyword,
            @Param("category") String category
    );
    
    // 📌 region_id로 여행지 조회 (지역별 랭킹용)
    List<TripVo> findByRegionId(@Param("regionId") Integer regionId);
}
