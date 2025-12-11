package com.insightcrew.domain.board.entity;

import java.time.LocalDateTime;

import com.insightcrew.domain.enums.BoardCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardEntity {
	private Long boardId;
	private Long memberId;
	private Long tripId;
	private BoardCategory category;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
