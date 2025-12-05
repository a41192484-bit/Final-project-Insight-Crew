package com.insightcrew.domain.member.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberJoinRequestDto {
	private String id;
	private String password;
	private String name;
	private String nickname;
}
