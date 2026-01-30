package com.sherwin.fintrac.launcher.integrationTest;

import com.sherwin.fintrac.launcher.config.CommonConfig;
import com.sherwin.fintrac.launcher.config.TransactionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication(scanBasePackages = "com.sherwin.fintrac")
@EnableJpaRepositories(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.repository"})
@EntityScan(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.entity"})
public class TestConfig {
  static void main(){
    SpringApplication.run(TestConfig.class);
  }
}
