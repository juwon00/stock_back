package com.stock.ohlcv;

import com.stock.stocks.KoreaStocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OhlcvRepository extends JpaRepository<Ohlcv, Long> {

    List<Ohlcv> findAllByKoreaStocks(KoreaStocks koreaStocks);
}
