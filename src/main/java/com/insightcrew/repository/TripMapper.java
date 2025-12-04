package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.trip.vo.TripVo;

@Mapper
public interface TripMapper {

    int existsByContentId(String contentId);

    void insert(TripVo tripVo);

    List<TripVo> findAll();

    TripVo findById(Long tripId);

    List<TripVo> findPage(@Param("offset") int offset, @Param("size") int size);

    int countAll();
}
