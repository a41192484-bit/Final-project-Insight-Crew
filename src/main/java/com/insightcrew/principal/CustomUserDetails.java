package com.insightcrew.principal;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.insightcrew.domain.member.vo.MemberVo;

import lombok.Getter;



@Getter
public class CustomUserDetails implements UserDetails {
	//로그인 후 시큐리티가 세션에 저장하는 객체\ 
	
	private final MemberVo membervo;
	
	public CustomUserDetails(MemberVo membervo) {
		this.membervo = membervo;
	}
	
	
	//시큐리티를 권한 객체로 바꿈
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		String role = membervo.getRole().name();
		return List.of(new SimpleGrantedAuthority("ROLE_"+role));
	}
	
	//비밀번호
	@Override
	public String getPassword() {
		return membervo.getPassword();
	}
	
	//아이디
	@Override
	public String getUsername() {
		return membervo.getUserid();
	}
	
	//닉네임
	public String getNickname() {
		return membervo.getNickname();
	}
	
	
	
	
	@Override
	public boolean isAccountNonExpired() { return true; }
	
	@Override
	public boolean isCredentialsNonExpired() { return true; }
	
	@Override
	public boolean isAccountNonLocked() { return true; }
	
	@Override
	public boolean isEnabled() { return true;}
	
	
	
	
	
	
	//스프링 시큐리티에서 세션 사용 시 warning 뜨는 것을 방지함.
//	private static final long serialVersionUID = 1L;
//	
//		
//	private final MemberVo membervo;
	
//	public CustomUserDetails(MemberVo membervo) {
//		this.membervo = membervo;
//	}
	
	//스프링시큐리티 권한객체로 변환
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities(){
//		return List.of(new SimpleGrantedAuthority("ROLE_"+membervo.getRole().name()));
//	}

}
