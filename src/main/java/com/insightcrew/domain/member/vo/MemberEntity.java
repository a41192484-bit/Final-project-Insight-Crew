package com.insightcrew.domain.member.vo;

import java.time.LocalDateTime;

import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEntity {
	private Long pkId;
	
	private String userid;
	private String password;
	private String name;
	private String nickname;
	
	private UserRole role;
	private UserStatus status;
	
	private LocalDateTime indate;
	private LocalDateTime outdate;
}
