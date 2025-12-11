package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.board.dto.BoardDetailResponse;
import com.insightcrew.domain.board.dto.BoardListResponse;
import com.insightcrew.domain.board.dto.BoardRequest;

@Mapper
public interface BoardMapper {

	int write(BoardRequest boardrequest);

	BoardDetailResponse detail(Long BoardId);

	int delete(Long BoardId);

	List<BoardListResponse> findAll();

	BoardRequest findById(Long id);

	void update(BoardRequest request);

	List<BoardListResponse> findTitle(@Param("keyword") String keyword);

	List<BoardListResponse> findMember(@Param("keyword") String keyword);

	List<BoardListResponse> findContent(@Param("keyword") String keyword);

	void saveBoard(BoardRequest request);
}
