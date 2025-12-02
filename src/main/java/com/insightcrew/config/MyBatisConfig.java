package com.insightcrew.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

// MyBatis 전용 설정 (mapper 경로 지정, typeAliases, camelCase 옵션 등)
@Configuration
@MapperScan(basePackages = "com.insightcrew.repository")
public class MyBatisConfig {
}