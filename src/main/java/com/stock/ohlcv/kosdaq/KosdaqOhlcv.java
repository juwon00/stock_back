package com.stock.ohlcv.kosdaq;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class KosdaqOhlcv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kosdaq_ohlcv_id")
    private Long id;

    private LocalDate date;

    private int open;

    private int high;

    private int low;

    private int close;

    private int volume;
}
