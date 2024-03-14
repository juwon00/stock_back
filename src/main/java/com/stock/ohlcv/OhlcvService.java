package com.stock.ohlcv;

import com.stock.ohlcv.dto.OhlcvDto;
import com.stock.stocks.KoreaStocks;
import com.stock.stocks.KoreaStocksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OhlcvService {

    private final KoreaStocksRepository koreaStocksRepository;
    private final OhlcvRepository ohlcvRepository;

    public List<OhlcvDto> getStocks(String value) {
        KoreaStocks stock;
        if (value.matches("[0-9]{6}")) {
            stock = koreaStocksRepository.findByCode(value);
        } else {
            stock = koreaStocksRepository.findByName(value);
        }
        return ohlcvRepository.findAllByKoreaStocks(stock).stream().map(this::convertToDto).toList();
    }

    private OhlcvDto convertToDto(Ohlcv kospiOhlcv) {
        return new OhlcvDto(kospiOhlcv.getOpen(), kospiOhlcv.getHigh(), kospiOhlcv.getLow(), kospiOhlcv.getClose(), kospiOhlcv.getVolume(), kospiOhlcv.getDate());
    }
}
