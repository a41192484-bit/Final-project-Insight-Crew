package com.insightcrew.domain.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BoardFileResponse {

	private Long id;
	private Long boardId;
	private String originalName;
	private String savedName;

}
