package com.stock.config;

import com.stock.ohlcv.Ohlcv;
import com.stock.ohlcv.OhlcvRepository;
import com.stock.stocks.KoreaStocks;
import com.stock.stocks.KoreaStocksRepository;
import com.stock.upbit.Coin;
import com.stock.upbit.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final OhlcvRepository ohlcvRepository;
    private final KoreaStocksRepository koreaStocksRepository;
    private final CoinRepository coinRepository;

    @Scheduled(cron = "0 10 18 * * 1-5", zone = "Asia/Seoul") // 주중 18시 10분 마다 실행
    public void getTodayOhlcv() throws IOException {
        log.info("005930 start");
        String code = "005930";
        List<Integer> todayOhlcv = getTodayOhlcv(code);
        saveTodayOhlcv(code, todayOhlcv);
        log.info(code + "save");
    }

    @Scheduled(cron = "0 35 * * * *", zone = "Asia/Seoul") // 매일 **시 35분 마다 실행 -> **시 00분 데이터 저장
    public void getCoin1() throws IOException {
        log.info("coin start");
        coinSave();
        log.info("coin finish");
    }

    @Scheduled(cron = "0 5 * * * *", zone = "Asia/Seoul") // 매일 **시 5분마다 실행 -> **-1시 30분 데이터 저장
    public void getCoin2() throws IOException {
        log.info("coin start");
        coinSave();
        log.info("coin finish");
    }

    private void coinSave() throws IOException {
        String pythonScriptPath = "py/upbit/get_today_coin_data.py";

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
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);

            String[] parts = line.split(" ");
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
        // 프로세스가 끝날 때까지 기다림
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 프로세스 종료
        process.destroy();
    }


    private void saveTodayOhlcv(String code, List<Integer> todayOhlcv) {
        if (todayOhlcv.size() >= 5) {
            LocalDate now = LocalDate.now();
            KoreaStocks koreaStocks = koreaStocksRepository.findByCode(code);
            Ohlcv ohlcv = new Ohlcv(now, todayOhlcv.get(0), todayOhlcv.get(1), todayOhlcv.get(2), todayOhlcv.get(3), todayOhlcv.get(4), koreaStocks);
            ohlcvRepository.save(ohlcv);
        } else {
            log.info("todayOhlcv 사이즈가 5보다 작습니다.");
        }
    }


    private List<Integer> getTodayOhlcv(String code) throws IOException {

        String pythonScriptPath = "py/get_today_ohlcv.py";

        // 파이썬 프로세스 실행
        ProcessBuilder pb = new ProcessBuilder("python3", pythonScriptPath, code);
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
        List<Integer> todayStock = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
            todayStock.add(Integer.parseInt(line));
        }

        // 프로세스가 끝날 때까지 기다림
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 프로세스 종료
        process.destroy();

        // 오늘의 시가 고가 저가 종가 거래량 순인 List<Integer> 반환
        return todayStock;
    }
}
