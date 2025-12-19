package com.insightcrew.domain.auth.dto;

import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
public class LoginResponseDto {
	private Long pkId;
	private String userid;
	private UserRole role;
}
