package com.mzc.lms.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CourseManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagementServiceApplication.class, args);
    }
}
