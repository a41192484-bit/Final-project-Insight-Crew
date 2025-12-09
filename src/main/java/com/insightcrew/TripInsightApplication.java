package com.insightcrew;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.insightcrew.repository")
@SpringBootApplication
public class TripInsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripInsightApplication.class, args);
	}

}
