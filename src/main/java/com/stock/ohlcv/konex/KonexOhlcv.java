package com.stock.ohlcv.konex;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class KonexOhlcv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kospi_ohlcv_id")
    private Long id;

    private LocalDate date;

    private int open;

    private int high;

    private int low;

    private int close;

    private int volume;
}
