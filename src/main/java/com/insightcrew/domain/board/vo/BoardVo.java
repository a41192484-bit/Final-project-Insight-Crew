package com.insightcrew.domain.board.vo;

import java.time.LocalDateTime;

import com.insightcrew.domain.board.enums.BoardCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardVo {
	private Long boardId;
	private Long memberId;
	private Long tripId;
	private BoardCategory category;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
