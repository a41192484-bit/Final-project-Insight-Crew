package com.insightcrew.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
	public boolean success;
	private String id;
	public String message;
}
