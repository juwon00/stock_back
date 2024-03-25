package com.stock.upbit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_id")
    private Long id;

    private LocalDateTime dateTime;

    private float open;

    private float high;

    private float low;

    private float close;

    private float volume;

    private float sma_60;

    private float sma_120;

    private float sma_200;

    private float upper;

    private float miidle;

    private float lower;

    private float macd;

    private float macd_signal;

    private float macd_hist;

    private float adx;

    private float rsi;

    private float slowk;

    private float slowd;

    public Coin(LocalDateTime dateTime, float open, float high, float low, float close, float volume, float sma_60, float sma_120, float sma_200, float upper, float miidle, float lower, float macd, float macd_signal, float macd_hist, float adx, float rsi, float slowk, float slowd) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.sma_60 = sma_60;
        this.sma_120 = sma_120;
        this.sma_200 = sma_200;
        this.upper = upper;
        this.miidle = miidle;
        this.lower = lower;
        this.macd = macd;
        this.macd_signal = macd_signal;
        this.macd_hist = macd_hist;
        this.adx = adx;
        this.rsi = rsi;
        this.slowk = slowk;
        this.slowd = slowd;
    }
}
