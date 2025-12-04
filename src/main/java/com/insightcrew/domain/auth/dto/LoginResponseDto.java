package com.insightcrew.domain.auth.dto;

import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;

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
	private String message;

	private Long pkId;
	private String userid;
	private String name;
	private String nickname;
	
	private UserRole role;
	private UserStatus status;
}
