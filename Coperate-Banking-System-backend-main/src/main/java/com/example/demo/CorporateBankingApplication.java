package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
public class CorporateBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorporateBankingApplication.class, args);
	}

}
