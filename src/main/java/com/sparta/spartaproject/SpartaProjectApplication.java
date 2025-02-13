package com.sparta.spartaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpartaProjectApplication.class, args);
    }
}