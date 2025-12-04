package com.insightcrew.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.repository.TripMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;

    public List<TripVo> findAll() {
        return tripMapper.findAll();
    }

    public TripVo findById(Long id) {
        return tripMapper.findById(id);
    }
    
    public List<TripVo> getTripPage(int page, int size) {
        int offset = (page - 1) * size;
        return tripMapper.findPage(offset, size);
    }

    public int getTotalPages(int size) {
        int totalCount = tripMapper.countAll();
        return (int) Math.ceil((double) totalCount / size);
    }
}
