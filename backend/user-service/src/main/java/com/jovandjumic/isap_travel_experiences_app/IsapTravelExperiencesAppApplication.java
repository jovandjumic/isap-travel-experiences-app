package com.jovandjumic.isap_travel_experiences_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class IsapTravelExperiencesAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(IsapTravelExperiencesAppApplication.class, args);
	}

}
