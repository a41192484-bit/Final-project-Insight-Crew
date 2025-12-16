package com.insightcrew.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.repository.TripRegionMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripRegionController {

    private final TripRegionMapper regionMapper;

    @GetMapping("/api/regions/sido")
    public List<String> getSido() {
        return regionMapper.findDistinctSido();
    }

    @GetMapping("/api/regions/sigungu")
    public List<String> getSigungu(@RequestParam(name = "sido") String sido) {
        return regionMapper.findSigunguBySido(sido);
    }
}
