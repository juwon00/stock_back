package com.stock.ohlcv.konex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class KonexOhlcv {

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
}
