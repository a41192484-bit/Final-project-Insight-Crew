package com.insightcrew.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MemberJoinResponseDto {
	private boolean success;
	private String message;
}
