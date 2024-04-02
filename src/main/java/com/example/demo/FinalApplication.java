package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FinalApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinalApplication.class, args);
	}

	@Bean
    CommandLineRunner run(BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            String encodedPassword = passwordEncoder.encode("1234");
            System.out.println("Encoded password: " + encodedPassword);
        };
    }
}
