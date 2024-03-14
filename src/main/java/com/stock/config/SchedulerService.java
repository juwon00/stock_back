package com.stock.config;

import com.stock.ohlcv.Ohlcv;
import com.stock.ohlcv.OhlcvRepository;
import com.stock.stocks.KoreaStocks;
import com.stock.stocks.KoreaStocksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final OhlcvRepository ohlcvRepository;
    private final KoreaStocksRepository koreaStocksRepository;

    @Scheduled(cron = "0 10 18 * * 1-5", zone = "Asia/Seoul") // 매일 18시 10분 마다 실행
    public void getTodayOhlcv() throws IOException {
        log.info("scheduler start");
        String code = "005930";
        List<Integer> todayOhlcv = getTodayOhlcv(code);
        saveTodayOhlcv(code, todayOhlcv);
        log.info(code + "save");
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
