package com.wanted.teamV;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TeamVApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamVApplication.class, args);
	}

}
