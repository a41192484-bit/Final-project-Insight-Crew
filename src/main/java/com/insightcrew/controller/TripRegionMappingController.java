// 지역매핑, 관리자 기능
package com.insightcrew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.service.TripRegionMappingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripRegionMappingController {

    private final TripRegionMappingService mappingService;

    @GetMapping("/admin/map-region")
    public String mapRegion() {

        mappingService.mapAllTripsRegion();

        return "지역 매핑 완료!";
    }
}
