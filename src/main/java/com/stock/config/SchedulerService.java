package com.stock.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulerService {

    @Scheduled(cron = "0 10 18 * * *") // 매일 18시 10분 마다 실행
    public void run() {
        log.info("scheduler start");

    }
}
