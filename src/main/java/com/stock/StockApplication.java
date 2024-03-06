package com.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing  // 자동으로 createdAt, modifiedAt 사용하기 위해
@EnableScheduling // 자동으로 스케줄러 사용하기 위해
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

}
