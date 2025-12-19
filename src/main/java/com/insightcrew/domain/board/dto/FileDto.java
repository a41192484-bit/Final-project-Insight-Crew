package com.insightcrew.domain.board.dto;

import lombok.Data;

@Data
public class FileDto {
	private Long id; // PK 파일 고유 id
	private Long boardId; // 어떤 게시글의 파일인지
	private String originalName; // 사용자가 업로드한 실제 파일명
	private String savedName; // 서버에 저장될 uuid 파일명
	private String filePath; // 파일이 실제 저장되는 경로
	private Long fileSize; // 파일 크기 (byte 단위)
}
