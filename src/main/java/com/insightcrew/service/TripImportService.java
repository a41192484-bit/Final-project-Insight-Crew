package com.insightcrew.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insightcrew.api.TripApiClient;
import com.insightcrew.domain.trip.dto.TripApiResponse;
import com.insightcrew.domain.trip.dto.TripApiResponse.Item;
import com.insightcrew.domain.trip.enums.TripCategory;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.repository.TripImportStatusMapper;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRegionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripImportService {

    private final TripApiClient apiClient;
    private final TripMapper tripMapper;
    private final TripImportStatusMapper statusMapper;
    private final ObjectMapper mapper = new ObjectMapper();
    private final TripRegionMapper regionMapper;

    private static final String[] CONTENT_TYPES = { "12", "14", "15", "28", "38" };
    private static final int[] AREA_CODES = {
            1,2,3,4,5,6,7,8,9,
            31,32,33,34,35,36,37,38,39
    };

    /** 전체 import */
    public void importAll() {

        for (String contentTypeId : CONTENT_TYPES) {
            for (int areaCode : AREA_CODES) {

                System.out.println("▶ import start: contentTypeId=" + contentTypeId +
                        ", areaCode=" + areaCode);

                importByTypeAndArea(contentTypeId, areaCode);

                System.out.println("■ import end  : contentTypeId=" + contentTypeId +
                        ", areaCode=" + areaCode);
            }
        }
    }

    /** 단일 타입/지역 import (이어받기 포함) */
    private void importByTypeAndArea(String contentTypeId, int areaCode) {

        Map<String, Object> status = statusMapper.getImportStatus();
        int startPage = 1;

        if (status != null &&
                contentTypeId.equals(String.valueOf(status.get("content_type_id"))) &&
                areaCode == ((Number) status.get("area_code")).intValue()) {

            startPage = ((Number) status.get("last_page")).intValue() + 1;

            System.out.println("▶ 이어서 시작합니다: contentType=" +
                    contentTypeId + ", areaCode=" + areaCode +
                    ", startPage=" + startPage);
        }

        int page = startPage;
        int emptyCount = 0;

        while (true) {

            TripApiResponse response = apiClient.search(contentTypeId, areaCode, page);

            if (response == null ||
                    response.getResponse() == null ||
                    response.getResponse().getBody() == null) {

                System.out.println("⚠ body null → skip page=" + page);
                if (++emptyCount >= 5) break;
                page++;
                continue;
            }

            var body = response.getResponse().getBody();
            var rawItems = body.getItems();

            if (!(rawItems instanceof Map)) {
                System.out.println("⚠ items 구조 비정상 → skip page=" + page);
                if (++emptyCount >= 5) break;
                page++;
                continue;
            }

            TripApiResponse.Items itemsObj = convertMapToItems(rawItems);

            if (itemsObj == null || itemsObj.getItem() == null) {
                System.out.println("⚠ items 변환 실패 → skip page=" + page);
                if (++emptyCount >= 5) break;
                page++;
                continue;
            }

            List<Item> items = itemsObj.getItem();

            if (items.isEmpty()) {
                System.out.println("⚠ empty page → skip page=" + page);
                if (++emptyCount >= 5) break;
                page++;
                continue;
            }

            emptyCount = 0;

            for (Item item : items) {
                saveTrip(item);
            }

            // 이어받기 상태 저장
            statusMapper.updateImportStatus(contentTypeId, areaCode, page);

            page++;
        }
    }

    /** 단일 여행지 저장 */
    private void saveTrip(Item item) {

        if (item == null || item.getContentid() == null) return;

        if (tripMapper.existsByContentId(item.getContentid()) > 0) return;

        TripVo vo = new TripVo();

        vo.setContentId(item.getContentid());
        vo.setName(item.getTitle());
        vo.setRegionCode(item.getAreacode());
        vo.setContentTypeId(item.getContenttypeid());
        vo.setLat(toDouble(item.getMapy()));
        vo.setLon(toDouble(item.getMapx()));

        String fullAddress =
                (item.getAddr1() == null ? "" : item.getAddr1()) + " " +
                (item.getAddr2() == null ? "" : item.getAddr2());
        vo.setFullAddress(fullAddress);

        vo.setImageUrl(item.getFirstimage());
        vo.setDetailImageUrl(item.getFirstimage2());
        vo.setTel(item.getTel());
        vo.setHomepage(item.getHomepage());
        vo.setDescription(item.getOverview());
        vo.setCategory(TripCategory.fromApi(item.getContenttypeid(), item.getCat1()).name());

        // ★ region_id 매핑
        vo.setRegionId(extractRegionId(fullAddress));

        // 저장
        tripMapper.insert(vo);
    }
    
    private Integer extractRegionId(String fullAddress) {

        if (fullAddress == null || fullAddress.isBlank()) {
            return null;
        }

        String[] parts = fullAddress.split(" ");

        if (parts.length < 2) return null;

        // ★ sido 정규화 (핵심!)
        String sido = normalizeSido(parts[0]);

        // 예: 강남구, 수원시, 해운대구 등
        String sigungu = parts[1];

        var region = regionMapper.findBySidoAndSigungu(sido, sigungu);

        return (region != null) ? region.getId() : null;
    }
    
    private String normalizeSido(String sido) {
        if (sido == null) return null;

        return sido.replace("특별시", "")
                   .replace("광역시", "")
                   .replace("특별자치시", "")
                   .replace("특별자치도", "")
                   .replace("도", "")
                   .trim();
    }
    
    private Double toDouble(String value) {
        try {
            return (value == null || value.isBlank()) ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** items 구조 정규화 */
    @SuppressWarnings("unchecked")
    private TripApiResponse.Items convertMapToItems(Object rawItems) {

        try {
            Map<String, Object> map = (Map<String, Object>) rawItems;
            Object itemList = map.get("item");

            TripApiResponse.Items items = new TripApiResponse.Items();
            List<TripApiResponse.Item> result = new java.util.ArrayList<>();

            if (itemList instanceof List<?> list) {

                for (Object obj : list) {
                    TripApiResponse.Item converted = convertToItem(obj);
                    if (converted != null) result.add(converted);
                }

            } else if (itemList instanceof Map<?, ?> single) {

                TripApiResponse.Item converted = convertToItem(single);
                if (converted != null) result.add(converted);
            }

            items.setItem(result);
            return items;

        } catch (Exception e) {
            System.out.println("⚠ convertMapToItems 에러");
            return null;
        }
    }

    /** raw map → Item 변환 */
    private TripApiResponse.Item convertToItem(Object obj) {
        try {
            return mapper.convertValue(obj, TripApiResponse.Item.class);
        } catch (Exception e) {
            return null;
        }
    }

}