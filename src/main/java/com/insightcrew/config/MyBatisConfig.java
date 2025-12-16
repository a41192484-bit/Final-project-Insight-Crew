package com.insightcrew.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration; 
 
@Configuration
@MapperScan(basePackages = "com.insightcrew.repository")
public class MyBatisConfig {

}
