package com.stock.upbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CoinScheduler {

    private final CoinService coinService;

    @Scheduled(cron = "02 00 * * * *", zone = "Asia/Seoul")  // 매시 00분 02초마다
    public void btcScheduler1() throws IOException, NoSuchAlgorithmException {
        log.info("btc start");
        String status = checkSuperTrend();
        if (status.equals("buy")) {
            String balance = coinService.getBalance();
            coinService.buy(balance);
            log.info("buy");
        } else if (status.equals("sell")) {
            String balance = coinService.getBalance();
            coinService.sell(balance);
            log.info("sell");
        }
        log.info("btc finish");
    }

    @Scheduled(cron = "02 30 * * * *", zone = "Asia/Seoul")  // 매시 30분 02초마다
    public void btcScheduler2() throws IOException, NoSuchAlgorithmException {
        log.info("btc start");
        String status = checkSuperTrend();
        if (status.equals("buy")) {
            String balance = coinService.getBalance();
            coinService.buy(balance);
            log.info("buy");
        } else if (status.equals("sell")) {
            String balance = coinService.getBalance();
            coinService.sell(balance);
            log.info("sell");
        }
        log.info("btc finish");
    }


    private String checkSuperTrend() throws IOException {

        String pythonScriptPath = "py/upbit/trading_judge.py";

        // 파이썬 프로세스 실행
        ProcessBuilder pb = new ProcessBuilder("python3", pythonScriptPath);
        Process process = pb.start();

        // 오류 메시지 출력 (존재 한다면)
        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        String errorline;
        while ((errorline = errorReader.readLine()) != null) {
            log.info(errorline);
        }

        // 파이썬 프로세스의 출력을 읽기 위한 BufferedReader 생성
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // 파이썬 프로세스의 출력을 읽어와서 자바에서 출력
        String status = null;
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
            status = line;
        }

        // 프로세스가 끝날 때까지 기다림
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 프로세스 종료
        process.destroy();

        return status;
    }
}
