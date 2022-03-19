package com.deukgeun.workout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class WorkoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkoutApplication.class, args);
    }

}
