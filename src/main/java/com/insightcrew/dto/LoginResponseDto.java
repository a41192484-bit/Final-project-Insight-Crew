package com.insightcrew.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
	private boolean success;
	private String id;
	private String message;
	private String role;
}
