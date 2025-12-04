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

	private static final String[] CONTENT_TYPES = { "12", "14", "15", "28", "38" };

	private static final int[] AREA_CODES = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 31, 32, 33, 34, 35, 36, 37, 38, 39 };

	public void importAll() {

		for (String contentTypeId : CONTENT_TYPES) {
			for (int areaCode : AREA_CODES) {
				importByTypeAndArea(contentTypeId, areaCode);
			}
		}
	}

	private void importByTypeAndArea(String contentTypeId, int areaCode) {

		int page = 1;

		while (true) {

			TripApiResponse response = apiClient.search(contentTypeId, areaCode, page);

			if (response == null || response.getResponse() == null || response.getResponse().getBody() == null) {
				break;
			}

			Object rawItems = response.getResponse().getBody().getItems();

			// ★ 핵심: items가 "" 또는 null 이면 다음 페이지로 넘어간다.
			if (!(rawItems instanceof java.util.Map)) {
				page++;
				continue;
			}

			// ★ items를 정상 구조로 변환
			TripApiResponse.Items itemsObj = convertMapToItems(rawItems);

			if (itemsObj == null || itemsObj.getItem() == null || itemsObj.getItem().isEmpty()) {
				break;
			}

			List<TripApiResponse.Item> items = itemsObj.getItem();

			for (TripApiResponse.Item item : items) {
				saveTrip(item);
			}

			page++;
		}
	}

	private void saveTrip(Item item) {

		if (tripMapper.existsByContentId(item.getContentid()) > 0) {
			return;
		}

		TripVo tripVo = new TripVo();

		tripVo.setContentId(item.getContentid());
		tripVo.setName(item.getTitle());
		tripVo.setRegionCode(item.getAreacode());
		tripVo.setContentTypeId(item.getContenttypeid());
		tripVo.setLat(toDouble(item.getMapy()));
		tripVo.setLon(toDouble(item.getMapx()));

		tripVo.setFullAddress((item.getAddr1() == null ? "" : item.getAddr1()) + " "
				+ (item.getAddr2() == null ? "" : item.getAddr2()));

		tripVo.setImageUrl(item.getFirstimage());
		tripVo.setDetailImageUrl(item.getFirstimage2());
		tripVo.setTel(item.getTel());
		tripVo.setHomepage(item.getHomepage());
		tripVo.setDescription(item.getOverview());

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
				// 정상적인 경우: item이 List로 전달됨
				List<TripApiResponse.Item> result = new ArrayList<>();
				for (Object obj : list) {
					result.add(convertToItem(obj));
				}
				items.setItem(result);
			} else if (itemList instanceof Map<?, ?> single) {
				// item이 1개인데 JSON에서 map으로 오는 경우
				List<TripApiResponse.Item> result = new ArrayList<>();
				result.add(convertToItem(single));
				items.setItem(result);
			}

			return items;

		} catch (Exception e) {
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
}
