package com.stock.upbit;

import jakarta.transaction.Transactional;
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
    private final CoinRepository coinRepository;

    @Transactional
    @Scheduled(cron = "0 0 0/2 * * *", zone = "Asia/Seoul")  // 00시 00분 02초마다
    public void btcScheduler1() throws IOException, NoSuchAlgorithmException {
        String status = checkSuperTrend();

        if (status.contains("buy")) {

            Coin coin = coinRepository.findById(1L).get();
            double currentPrice = Float.parseFloat(status.split(" ")[1]);

            if (!coin.isBuy()) {
                coinService.buy(coinService.getBalance());
                coinRepository.updateBuy(currentPrice);
                log.info("buy");
            } else {
                log.info("buy error");
            }
        } else if (status.equals("sell")) {

            Coin coin = coinRepository.findById(1L).get();

            if (coin.isBuy()) {
                coinService.sell(coinService.getBalance());
                coinRepository.updateSell();
                log.info("sell");
            } else {
                log.info("sell error");
            }
        } else if (status.contains("hold")) {

            Coin coin = coinRepository.findById(1L).get();
            double currentPrice = Float.parseFloat(status.split(" ")[1]);

            if (((currentPrice / coin.getPurchasePrice()) - 1) * 100 <= -3.2 && coin.isBuy()) {
                coinService.sell(coinService.getBalance());
                coinRepository.updateSell();
                log.info("stop loss");
            } else {
                log.info("hold");
            }
        }
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
