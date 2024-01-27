package com.stock.ohlcv.kosdaq;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class KosdaqOhlcv {

    @Id
    @GeneratedValue
    @Column(name = "kosdaq_ohlcv_id")
    private Long id;

    private LocalDate date;

    private int open;

    private int high;

    private int low;

    private int close;

    private int volume;
}
