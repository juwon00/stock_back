package com.stock.ohlcv.kospi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KospiOhlcvRepository extends JpaRepository<KospiOhlcv, Long> {
}
