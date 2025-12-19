package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.trip.dto.TripDetailResponse;
import com.insightcrew.domain.trip.vo.TripVo;

@Mapper
public interface TripMapper {

    int existsByContentId(String contentId);
    void insert(TripVo tripVo);
    List<TripVo> findAll();
    TripVo findById(Long tripId);
    List<TripVo> findPage(@Param("offset") int offset, @Param("size") int size);
    int countAll();
    TripDetailResponse findTripDetailById(Long tripId);

    // 신규: 지역검색 포함
    List<TripVo> searchTrips(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            @Param("offset") int offset,
            @Param("limit") int limit
    );


    int countTrips(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu
    );

    // 전국 랭킹용
    List<TripVo> findByRegionId(@Param("regionId") Integer regionId);

    // region_id 업데이트
    void updateRegionId(@Param("tripId") Long tripId, @Param("regionId") Integer regionId);

    // 여행지 전체 import 이어받기 관리
//    Object getImportStatus();
//    void updateImportStatus(
//            @Param("contentTypeId") String contentTypeId,
//            @Param("areaCode") String areaCode,
//            @Param("lastPage") Integer lastPage
//    );
    
    void updateRegionInfo(
    	    @Param("tripId") Long tripId,
    	    @Param("sido") String sido,
    	    @Param("sigungu") String sigungu,
    	    @Param("regionId") Integer regionId
    	);
}
