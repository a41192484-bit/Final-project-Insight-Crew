package com.insightcrew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
    	//웹사이트에서 보내는 악의적인 공격을 보호함. 근데 여기서는 disable()로 끔.
        .csrf(csrf -> csrf.disable())
        //페이지별 사이트url별로 접근 권한 설정
        .authorizeHttpRequests(auth -> auth
        				//어떤 url이 대상인지
                        .requestMatchers("/", "/login/**", "/css/**", "/js/**", "/img/**")
                        .permitAll()
                        .requestMatchers("/board/list").hasRole("USER") // USER 권한 필요
                        .requestMatchers("/java").hasRole("ADMIN") // ADMIN 권한 필요
                        .anyRequest().authenticated() // 그 외 모든 요청 인증 필요
        )
        .formLogin(login -> login
                        .loginPage("/login/login") // 커스텀 로그인 페이지
                        .loginProcessingUrl("/login") // POST 로그인 처리 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동
                        .failureUrl("/login/login?error") // 로그인 실패 시 이동
                        .permitAll())
        .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청 URL
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                        .permitAll()

        );

        return http.build();
    }
}
