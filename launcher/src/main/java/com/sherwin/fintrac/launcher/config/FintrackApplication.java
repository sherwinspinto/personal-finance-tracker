package com.sherwin.fintrac.launcher.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.sherwin.fintrac")
@EnableJpaRepositories(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.repository"})
@EntityScan(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.entity"})
public class FintrackApplication {
    static void main(String[] args) {
        SpringApplication.run(FintrackApplication.class, args);
    }
}
