package com.insightcrew.domain.board.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BoardRequest { // 작성 수정용
	private Long boardId;
	private Long memberId;
	private Long tripId;
	private String category;
	private String title;
	private String content;

	private List<MultipartFile> files; // 첨부파일 추가
}