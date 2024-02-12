package com.stock.config;

import com.stock.stocks.KoreaStocks;
import com.stock.stocks.KoreaStocksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
//@Component  // 주석처리 함으로서 서버가 실행될 때 작동하지 않음
@RequiredArgsConstructor
public class ApplicationRunnerBean implements ApplicationRunner {

    private final KoreaStocksRepository koreaStocksRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("test");
        InputStream inputStream = getClass().getResourceAsStream("/krx/KRX_2024_02_13.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String code = parts[0];
                String name = parts[1];
                String market = parts[2];

                KoreaStocks koreaStocks = new KoreaStocks(code, name, market);
                koreaStocksRepository.save(koreaStocks);
            }
        }
        log.info("finish");
    }
}
