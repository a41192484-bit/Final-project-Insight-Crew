package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.board.dto.BoardDetailResponseDto;
import com.insightcrew.domain.board.dto.BoardListResponseDto;
import com.insightcrew.domain.board.dto.BoardRequestDto;

@Mapper
public interface BoardMapper {

	int write(BoardRequestDto boardrequest);

	BoardDetailResponseDto detail(Long BoardId);

	int delete(Long BoardId);

	List<BoardListResponseDto> findAll();

	BoardRequestDto findById(Long id);

	void update(BoardRequestDto request);

	List<BoardListResponseDto> findTitle(@Param("keyword") String keyword);

	List<BoardListResponseDto> findMember(@Param("keyword") String keyword);

	List<BoardListResponseDto> findContent(@Param("keyword") String keyword);

	void saveBoard(BoardRequestDto request);
	
	// 메인화면 최신글 조회 기능 (추가)
	List<BoardListResponseDto> findLatest(int limit);
	
	// 관리자 페이지 대시보드 전체 게시글 수 (추가)
	int countAll();
}
