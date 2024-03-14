package com.stock.ohlcv.kospi;

import com.stock.stocks.KoreaStocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KospiOhlcvRepository extends JpaRepository<KospiOhlcv, Long> {

    List<KospiOhlcv> findAllByKoreaStocks(KoreaStocks koreaStocks);
}
