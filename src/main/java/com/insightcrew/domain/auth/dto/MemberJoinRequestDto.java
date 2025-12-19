package com.insightcrew.domain.auth.dto;

import com.insightcrew.domain.member.enums.UserRole;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberJoinRequestDto {
	private String userid;
	private String password;
	private String name;
	private String nickname;
	private UserRole role;
}
