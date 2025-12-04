package com.insightcrew.domain.member.vo;

import java.time.LocalDateTime;

import com.insightcrew.enums.UserRole;
import com.insightcrew.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
	private Long pkId;
	
	private String id;
	private String password;
	private String name;
	private String nickname;
	
	private UserRole role;
	private UserStatus status;
	
	private LocalDateTime indate;
	private String outdate;
}
