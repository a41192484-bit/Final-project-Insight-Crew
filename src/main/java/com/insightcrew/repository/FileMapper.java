package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.insightcrew.domain.board.dto.FileDto;

@Mapper
public interface FileMapper {

	void insertFile(FileDto filedto); // 파일 정보를 db에 insert

	FileDto findById(Long id); // 특정 파일 조회(id)

	List<FileDto> findAll(); // 전체 파일 목록 조회

	void deleteFile(Long id); // 파일 삭제(id)
}
