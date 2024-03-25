package com.stock.upbit.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoinDTO {
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
}
