package com.stock.upbit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Coin c set c.isBuy = false, c.purchasePrice = 0")
    void updateSell();

    @Modifying(clearAutomatically = true)
    @Query("update Coin c set c.isBuy = true, c.purchasePrice = :purchasePrice")
    void updateBuy(double purchasePrice);
}
