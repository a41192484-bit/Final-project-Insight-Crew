package com.insightcrew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 스프링 시큐리티 전체 규칙(로그인/로그아웃/접근권한/인증방식 지정)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())  // 개발 중이니까 OK
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // 개발 단계용
                )
                .formLogin(form -> form.disable())   // 추후 로그인 구현하면 변경
                .logout(logout -> logout.disable());

        return http.build();
    }
}
