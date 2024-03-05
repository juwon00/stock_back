package com.stock.ohlcv.kospi;

import com.stock.stocks.KoreaStocks;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class KospiOhlcv {

    @Id
    @GeneratedValue
    @Column(name = "kospi_ohlcv_id")
    private Long id;

    private LocalDate date;

    private int open;

    private int high;

    private int low;

    private int close;

    private int volume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korea_stocks_id")
    private KoreaStocks koreaStocks;

    public KospiOhlcv(LocalDate date, int open, int high, int low, int close, int volume, KoreaStocks koreaStocks) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.koreaStocks = koreaStocks;
    }
}
