package com.insightcrew.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insightcrew.api.TripApiClient;
import com.insightcrew.domain.trip.dto.TripApiResponse;
import com.insightcrew.domain.trip.dto.TripApiResponse.Item;
import com.insightcrew.domain.trip.enums.TripCategory;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.repository.TripMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripImportService {

    private final TripApiClient apiClient;
    private final TripMapper tripMapper;

    // 관광공사 contentTypeId
    private static final String[] CONTENT_TYPES = { "12", "14", "15", "28", "38" };

    // 관광공사 areaCode
    private static final int[] AREA_CODES = {
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            31, 32, 33, 34, 35, 36, 37, 38, 39
    };

    // 안전장치: 한 타입/지역당 최대 몇 페이지까지 돌릴지
    private static final int MAX_PAGE = 200; // numOfRows=20이면 4000개까지 커버

    /**
     * 모든 contentTypeId × areaCode 조합에 대해 전체 데이터 수집
     */
    public void importAll() {

        for (String contentTypeId : CONTENT_TYPES) {
            for (int areaCode : AREA_CODES) {
                System.out.println("▶ import start: contentTypeId=" + contentTypeId + ", areaCode=" + areaCode);
                importByTypeAndArea(contentTypeId, areaCode);
                System.out.println("■ import end  : contentTypeId=" + contentTypeId + ", areaCode=" + areaCode);
            }
        }
    }

    /**
     * 특정 contentTypeId + areaCode 에 대해
     * 페이지를 끝까지 돌면서 여행지 정보 수집
     */
    private void importByTypeAndArea(String contentTypeId, int areaCode) {

        int page = 1;

        while (page <= MAX_PAGE) {

            TripApiResponse response = apiClient.search(contentTypeId, areaCode, page);

            // 🔥 API 호출 실패/NULL 응답 → 이 페이지만 스킵하고 다음 페이지로
            if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null) {

                System.out.println("⚠️ [TripImport] body null → contentTypeId=" + contentTypeId
                        + ", areaCode=" + areaCode + ", page=" + page + " → skip");
                sleep();
                page++;
                continue;
            }

            var body = response.getResponse().getBody();
            Object rawItems = body.getItems();

            // 🔥 items 구조 자체가 이상할 때(문자열, null 등) → 이 페이지만 스킵
            if (!(rawItems instanceof Map)) {
                System.out.println("⚠️ [TripImport] items 구조 비정상 → contentTypeId=" + contentTypeId
                        + ", areaCode=" + areaCode + ", page=" + page + " → skip");
                sleep();
                page++;
                continue;
            }

            TripApiResponse.Items itemsObj = convertMapToItems(rawItems);

            // 🔥 items 변환 실패 → 이 페이지만 스킵
            if (itemsObj == null || itemsObj.getItem() == null) {
                System.out.println("⚠️ [TripImport] items 변환 실패 → contentTypeId=" + contentTypeId
                        + ", areaCode=" + areaCode + ", page=" + page + " → skip");
                sleep();
                page++;
                continue;
            }

            List<Item> items = itemsObj.getItem();

            // 🔥 관광공사는 중간 페이지가 empty일 수도 있으니 종료조건 X, 그냥 스킵
            if (items.isEmpty()) {
                System.out.println("⚠️ [TripImport] empty page → contentTypeId=" + contentTypeId
                        + ", areaCode=" + areaCode + ", page=" + page + " → skip");
                sleep();
                page++;
                continue;
            }

            // 🔥 정상 데이터 insert
            for (Item item : items) {
                saveTrip(item);
            }

            sleep();
            page++;
        }
    }

    /**
     * 단일 여행지 저장
     * - contentId 기준 중복 방지
     */
    private void saveTrip(Item item) {

        if (item == null || item.getContentid() == null) {
            return;
        }

        if (tripMapper.existsByContentId(item.getContentid()) > 0) {
            // 이미 존재하면 스킵
            return;
        }

        TripVo tripVo = new TripVo();

        tripVo.setContentId(item.getContentid());
        tripVo.setName(item.getTitle());
        tripVo.setRegionCode(item.getAreacode());
        tripVo.setContentTypeId(item.getContenttypeid());
        tripVo.setLat(toDouble(item.getMapy()));
        tripVo.setLon(toDouble(item.getMapx()));

        tripVo.setFullAddress(
                (item.getAddr1() == null ? "" : item.getAddr1()) + " " +
                (item.getAddr2() == null ? "" : item.getAddr2())
        );

        tripVo.setImageUrl(item.getFirstimage());
        tripVo.setDetailImageUrl(item.getFirstimage2());
        tripVo.setTel(item.getTel());
        tripVo.setHomepage(item.getHomepage());
        tripVo.setDescription(item.getOverview());

        // 카테고리 매핑 (TripCategory.fromApi 는 너가 만든 로직 그대로 사용)
        tripVo.setCategory(TripCategory.fromApi(item.getContenttypeid(), item.getCat1()).name());

        tripMapper.insert(tripVo);
    }

    private Double toDouble(String value) {
        try {
            if (value == null || value.isBlank()) {
                return null;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private TripApiResponse.Items convertMapToItems(Object rawItems) {
        try {
            Map<String, Object> map = (Map<String, Object>) rawItems;
            Object itemList = map.get("item");

            TripApiResponse.Items items = new TripApiResponse.Items();

            if (itemList instanceof List<?> list) {
                // item이 List로 오는 경우
                List<TripApiResponse.Item> result = new ArrayList<>();
                for (Object obj : list) {
                    TripApiResponse.Item converted = convertToItem(obj);
                    if (converted != null) {
                        result.add(converted);
                    }
                }
                items.setItem(result);

            } else if (itemList instanceof Map<?, ?> single) {
                // item이 1개인데 Map으로 오는 경우
                List<TripApiResponse.Item> result = new ArrayList<>();
                TripApiResponse.Item converted = convertToItem(single);
                if (converted != null) {
                    result.add(converted);
                }
                items.setItem(result);
            } else {
                // item이 전혀 없거나 이상한 타입인 경우
                items.setItem(new ArrayList<>());
            }

            return items;

        } catch (Exception e) {
            System.out.println("⚠️ [TripImport] convertMapToItems 에러");
            return null;
        }
    }

    private TripApiResponse.Item convertToItem(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(obj, TripApiResponse.Item.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * API 과부하 방지용 딜레이
     */
    private void sleep() {
        try {
            Thread.sleep(180); // 0.18초 정도면 무난
        } catch (InterruptedException ignored) {
        }
    }
}
