package com.insightcrew.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.dto.TripDetailResponse;
import com.insightcrew.domain.trip.enums.TripCategory;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.repository.TripMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;

    /** 상세 조회 */
    public TripDetailResponse getTripDetail(Long tripId) {
        return tripMapper.findTripDetailById(tripId);
    }

    // ======================================================
    // 🔥 검색 + 카테고리 + 지역(sido, sigungu) + 페이징
    // ======================================================
    public List<TripVo> searchTrips(
            String keyword,
            String category,
            String sido,
            String sigungu,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;
        String categoryValue = convertToDbCategory(category);

        return tripMapper.searchTrips(
                keyword,
                categoryValue,
                sido,
                sigungu,
                offset,
                size
        );
    }

    /** 🔥 전체 페이지 수 계산 */
    public int getSearchTotalPages(
            String keyword,
            String category,
            String sido,
            String sigungu,
            int size
    ) {
        String categoryValue = convertToDbCategory(category);

        int totalCount = tripMapper.countTrips(
                keyword,
                categoryValue,
                sido,
                sigungu
        );

        return (int) Math.ceil((double) totalCount / size);
    }

    /** ENUM → DB category 변환 */
    private String convertToDbCategory(String category) {

        if (category == null || category.trim().isEmpty() || category.equals("ALL")) {
            return null;
        }

        try {
            TripCategory tripCategory = TripCategory.valueOf(category);
            return tripCategory.name();
        } catch (Exception e) {
            return null;
        }
    }
}
