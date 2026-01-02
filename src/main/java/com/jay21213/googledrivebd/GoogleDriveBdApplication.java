package com.jay21213.googledrivebd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@Configuration
@EnableMongoAuditing
public class GoogleDriveBdApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoogleDriveBdApplication.class, args);
    }

}
