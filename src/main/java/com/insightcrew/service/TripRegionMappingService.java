// 임시 실행용
package com.insightcrew.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.vo.RegionVo;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRegionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripRegionMappingService {

    private final TripMapper tripMapper;
    private final TripRegionMapper regionMapper;

    /**
     * 전체 여행지 지역 매핑 실행
     */
    public void mapAllTripsRegion() {

        List<TripVo> trips = tripMapper.findAll();
        int updated = 0;

        for (TripVo trip : trips) {

            // 이미 매핑되어 있어도 sido/sigungu 값이 NULL이면 다시 매핑해야 함
            String addr = trip.getFullAddress();
            if (addr == null || addr.isBlank()) continue;

            // 1) 주소에서 시도/시군구 추출
            String[] parsed = parseRegion(addr);
            String sido = parsed[0];
            String sigungu = parsed[1];

            if (sido == null) continue;

            // 2) region_id 구하기
            RegionVo region = regionMapper.findBySidoAndSigungu(sido, sigungu);
            Integer regionId = (region != null ? region.getId() : null);

            // 3) trip 테이블에 sido / sigungu / region_id 모두 업데이트
            tripMapper.updateRegionInfo(
                    trip.getTripId(),
                    sido,
                    sigungu,
                    regionId
            );

            updated++;
        }

        System.out.println("🎯 여행지 지역 매핑 완료: " + updated + "건 업데이트됨!");
    }

    /**
     * 주소에서 시도 / 시군구 추출
     * 예: "경기도 고양시 덕양구 무슨동" → ["경기도", "고양시"]
     */
    private String[] parseRegion(String fullAddr) {

        String sido = null;
        String sigungu = null;

        String[] parts = fullAddr.split(" ");

        if (parts.length >= 1) sido = parts[0];
        if (parts.length >= 2) sigungu = parts[1];

        return new String[]{sido, sigungu};
    }
}
