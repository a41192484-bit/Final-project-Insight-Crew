package com.insightcrew.domain.member.dto;

import com.insightcrew.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
	private boolean success;
	private String userid;
	private String message;
	private UserRole role;
}
