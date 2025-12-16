package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.trip.vo.RegionVo;

@Mapper
public interface TripRegionMapper {

    // 시·도 + 시군구 매핑 조회
    RegionVo findBySidoAndSigungu(@Param("sido") String sido,
                                  @Param("sigungu") String sigungu);

    // region_id 단일 조회
    RegionVo findById(@Param("id") Integer id);

    // 전체 지역 목록 조회 (날씨 업데이트용)
    List<RegionVo> findAll();


    // 시도 전체 조회 (드롭다운용)
    List<String> findDistinctSido();

    // 시도 선택 시 해당 시군구 조회
    List<String> findSigunguBySido(@Param("sido") String sido);
    
    // 전지역 중기예보
    List<String> findDistinctMidRegionCodes();
}
