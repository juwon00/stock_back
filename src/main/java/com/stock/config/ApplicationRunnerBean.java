package com.stock.config;

import com.stock.ohlcv.kospi.KospiOhlcv;
import com.stock.ohlcv.kospi.KospiOhlcvRepository;
import com.stock.stocks.KoreaStocks;
import com.stock.stocks.KoreaStocksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component  // 주석처리 함으로서 서버가 실행될 때 작동하지 않음
@RequiredArgsConstructor
public class ApplicationRunnerBean implements ApplicationRunner {

    private final KoreaStocksRepository koreaStocksRepository;
    private final KospiOhlcvRepository kospiOhlcvRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("test");

//        KoreaStocksConfig();
//        getOhlcv("005930");

        log.info("finish");
    }

    private void getOhlcv(String code) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/krx/kospi/" + code + "_2024_03_04.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                String dateStr = parts[0];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateStr, formatter);
                int open = Integer.parseInt(parts[1]);
                int high = Integer.parseInt(parts[2]);
                int low = Integer.parseInt(parts[3]);
                int close = Integer.parseInt(parts[4]);
                int volume = Integer.parseInt(parts[5]);

                KoreaStocks koreaStocks = koreaStocksRepository.findByCode(code);
                System.out.println("koreaStocks = " + koreaStocks + koreaStocks.getName() + koreaStocks.getCode());
                KospiOhlcv kospiOhlcv = new KospiOhlcv(date, open, high, low, close, volume, koreaStocks);
                kospiOhlcvRepository.save(kospiOhlcv);
            }
        }
    }

    private void KoreaStocksConfig() throws IOException {
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
    }
}
