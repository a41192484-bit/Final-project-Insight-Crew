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

	// pk id(고유식별자)는 세션, 인증, 권한 처리할 때 필수
	private Long pkId;
	private String userid;
	private String name;
	private String nickname;
	
	private UserRole role;
	private UserStatus status;
}
