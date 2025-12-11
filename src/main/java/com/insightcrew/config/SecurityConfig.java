package com.insightcrew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.insightcrew.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomUserDetailsService customUserDetailsService;
	
	//이거는 passwordEncoderConfig.java로 따로 빼도 됨.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	http
    	//웹사이트에서 보내는 악의적인 공격을 보호함. 근데 여기서는 disable()로 끔.
        .csrf(csrf -> csrf.disable())
        //페이지별 사이트url별로 접근 권한 설정
        .authorizeHttpRequests(auth -> auth
        				//어떤 url이 대상인지. permitAll()은 누구나 접근 허용
                        .requestMatchers("/auth/**", "/join/**", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/member/**").hasRole("USER") // USER 권한 필요
                        .requestMatchers("/mypage/**").hasRole("USER")
                        .requestMatchers("/java").hasRole("ADMIN") // ADMIN 권한 필요
                        .anyRequest().authenticated() // 그 외 모든 요청 인증 필요
        )
        .formLogin(login -> login
                        .loginPage("/auth") // 내가 만든 로그인 페이지 경로
                        .loginProcessingUrl("/auth/login-process") // POST 로그인 처리 URL. 시큐리티가 실제 인증 처리하는url
                        .usernameParameter("userid")  //인풋 값 이름 여기다가 지정
                        .passwordParameter("password") //인풋값 이름 여기다 지정
                        .defaultSuccessUrl("/member/member-mypage", true) // 로그인 성공 시 이동
                        .failureUrl("/auth/view?error=true") // 로그인 실패 시 이동
                        .permitAll()) //로그인 실패 후 다시 로그인 페이지로 접근할 수 있도록 하는 것.
        								//이래야 로그인 실패 메시지도 확인 가능.
        								//안 쓰면 오류 발생.
        .logout(logout -> logout
                        .logoutUrl("/auth/logout") // 로그아웃 요청 URL
                        .logoutSuccessUrl("/auth/view") // 로그아웃 후 리다이렉트
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                        .permitAll()

        );

        return http.build();
    }
}
