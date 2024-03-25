package com.stock.config;

import com.stock.ohlcv.Ohlcv;
import com.stock.ohlcv.OhlcvRepository;
import com.stock.stocks.KoreaStocks;
import com.stock.stocks.KoreaStocksRepository;
import com.stock.upbit.Coin;
import com.stock.upbit.CoinRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component  // 주석처리 하면 서버가 실행될 때 작동하지 않음
@RequiredArgsConstructor
public class ApplicationRunnerBean implements ApplicationRunner {

    private final KoreaStocksRepository koreaStocksRepository;
    private final OhlcvRepository ohlcvRepository;
    private final CoinRepository coinRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("test");

//        KoreaStocksConfig();
//        getOhlcv("005930");
//        getCoin();

        log.info("finish");
    }

    private void getOhlcv(String code) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/csv/krx/kospi/" + code + "_2024_03_13.csv");
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
                Ohlcv kospiOhlcv = new Ohlcv(date, open, high, low, close, volume, koreaStocks);
                ohlcvRepository.save(kospiOhlcv);
            }
        }
    }

    private void KoreaStocksConfig() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/csv/krx/KRX_2024_03_13.csv");
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

    private void getCoin() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/csv/upbit/data_2024_03_23~2024_03_24.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            float sma_60;
            float sma_120;
            float sma_200;
            float upper;
            float middle;
            float lower;
            float macd;
            float signal;
            float macd_hist;
            float adx;
            float rsi;
            float slowk;
            float slowd;

            if (parts.length >= 18) {
                String dateStr = parts[0];
                LocalDateTime dateTime = LocalDateTime.parse(dateStr);
                float open = Float.parseFloat(parts[1]);
                float high = Float.parseFloat(parts[2]);
                float low = Float.parseFloat(parts[3]);
                float close = Float.parseFloat(parts[4]);
                float volume = Float.parseFloat(parts[5]);
                if (parts[6].equals("nan")) {
                    sma_60 = 0;
                } else {
                    sma_60 = Float.parseFloat(parts[6]);
                }
                if (parts[7].equals("nan")) {
                    sma_120 = 0;
                } else {
                    sma_120 = Float.parseFloat(parts[7]);
                }
                if (parts[8].equals("nan")) {
                    sma_200 = 0;
                } else {
                    sma_200 = Float.parseFloat(parts[8]);
                }
                if (parts[9].equals("nan")) {
                    upper = 0;
                } else {
                    upper = Float.parseFloat(parts[9]);
                }
                if (parts[10].equals("nan")) {
                    middle = 0;
                } else {
                    middle = Float.parseFloat(parts[10]);
                }
                if (parts[11].equals("nan")) {
                    lower = 0;
                } else {
                    lower = Float.parseFloat(parts[11]);
                }
                if (parts[12].equals("nan")) {
                    macd = 0;
                } else {
                    macd = Float.parseFloat(parts[12]);
                }
                if (parts[13].equals("nan")) {
                    signal = 0;
                } else {
                    signal = Float.parseFloat(parts[13]);
                }
                if (parts[14].equals("nan")) {
                    macd_hist = 0;
                } else {
                    macd_hist = Float.parseFloat(parts[14]);
                }
                if (parts[15].equals("nan")) {
                    adx = 0;
                } else {
                    adx = Float.parseFloat(parts[15]);
                }
                if (parts[16].equals("nan")) {
                    rsi = 0;
                } else {
                    rsi = Float.parseFloat(parts[16]);
                }
                if (parts[17].equals("nan")) {
                    slowk = 0;
                } else {
                    slowk = Float.parseFloat(parts[17]);
                }
                if (parts[18].equals("nan")) {
                    slowd = 0;
                } else {
                    slowd = Float.parseFloat(parts[18]);
                }
                Coin coin = new Coin(dateTime, open, high, low, close, volume, sma_60, sma_120, sma_200, upper, middle, lower, macd, signal, macd_hist, adx, rsi, slowk, slowd);
                coinRepository.save(coin);
            }
        }
    }

}
