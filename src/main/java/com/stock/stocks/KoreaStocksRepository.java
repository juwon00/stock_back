package com.stock.stocks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KoreaStocksRepository extends JpaRepository<KoreaStocks, Long> {
}
