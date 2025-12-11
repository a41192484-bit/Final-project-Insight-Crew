package com.insightcrew.domain.board.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardListResponse { // 보기용
	private Long boardId;
	private Long memberId;
	private String title;
	private LocalDateTime createdAt;

	private int viewOrder; // 화면 번호 표시용
}