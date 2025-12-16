package com.insightcrew.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TripImportStatusMapper {
    Map<String, Object> getImportStatus();
    void updateImportStatus(@Param("contentTypeId") String contentTypeId,
                            @Param("areaCode") int areaCode,
                            @Param("lastPage") int lastPage);
}
