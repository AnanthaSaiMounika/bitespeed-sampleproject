package com.bitespeed.sample_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SampleProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleProjectApplication.class, args);
    }
}
