package com.insightcrew.domain.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.insightcrew.domain.board.enums.BoardCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BoardDetailResponseDto { // 상세보기용
//	private Long boardId;
	private Long memberId;
	private Long tripId;
	private BoardCategory category;
	private String title;
	private String content;
	private LocalDateTime createdAt;

	private List<BoardFileResponseDto> files; // 첨부파일
}
