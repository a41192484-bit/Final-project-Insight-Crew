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

    /* ======================
       🔵 기본 조회
       ====================== */

    public List<TripVo> findAll() {
        return tripMapper.findAll();
    }

    public TripVo findById(Long id) {
        return tripMapper.findById(id);
    }

    /* ======================
       🔵 기본 페이징 목록
       ====================== */
    public List<TripVo> getTripPage(int page, int size) {
        int offset = (page - 1) * size;
        return tripMapper.findPage(offset, size);
    }

    public int getTotalPages(int size) {
        int totalCount = tripMapper.countAll();
        return (int) Math.ceil((double) totalCount / size);
    }

    /* ======================
       🔵 상세 조회
       ====================== */
    public TripDetailResponse getTripDetail(Long tripId) {
        return tripMapper.findTripDetailById(tripId);
    }

    /* ======================
       🔵 검색 + 카테고리 + 페이징 목록
       ====================== */

    public List<TripVo> searchTrips(String keyword, String category, int page, int size) {

        int offset = (page - 1) * size;

        String categoryValue = convertToDbCategory(category);
        
        return tripMapper.searchTrips(keyword, categoryValue, size, offset);
    }

    /* ===========================
       🔵 검색/카테고리 전체 페이지 수
       =========================== */
    public int getSearchTotalPages(String keyword, String category, int size) {

        String categoryValue = convertToDbCategory(category);

        int totalCount = tripMapper.countTrips(keyword, categoryValue);
        return (int) Math.ceil((double) totalCount / size);
    }

    /* ===========================
       🟣 ENUM → DB 값 변환 메서드 (핵심)
       =========================== */
    private String convertToDbCategory(String category) {
        if (category == null || category.trim().isEmpty() || category.equals("ALL")) {
            return null; // 전체 조회
        }

        try {
            // ENUM 이름을 ENUM 객체로 변환
            TripCategory tripCategory = TripCategory.valueOf(category);
            return tripCategory.getDisplayName(); 
            // 예: NATURE → "자연"
        } catch (Exception e) {
            return null; // 변환 실패 시 전체조회
        }
    }
}