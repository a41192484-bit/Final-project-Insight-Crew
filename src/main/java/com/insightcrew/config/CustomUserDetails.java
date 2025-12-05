package com.insightcrew.config;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.insightcrew.domain.member.enums.UserStatus;
import com.insightcrew.domain.member.vo.MemberVo;

import lombok.Getter;


@Getter
public class CustomUserDetails implements UserDetails {
	
	private final MemberVo membervo;
	
	public CustomUserDetails(MemberVo membervo) {
		this.membervo = membervo;
	}
	
	//스프링시큐리티 권한객체로 변환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		if (membervo.getRole() == null) {
	        return List.of();
	    }
		
		return List.of(new SimpleGrantedAuthority("ROLE_" + membervo.getRole().name()));
	}
	
	@Override
	public String getPassword() {
		return membervo.getPassword();
	}
	
	@Override
	public String getUsername() {
		return membervo.getUserid();
	}
	
	@Override
	public boolean isEnabled() {
		return membervo.getStatus() == UserStatus.ACTIVE;
	}
	
	@Override
	public boolean isAccountNonExpired() { return true; }
	
	@Override
	public boolean isCredentialsNonExpired() { return true; }
	
	@Override
	public boolean isAccountNonLocked() { return true; }
}
