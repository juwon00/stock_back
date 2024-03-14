package com.stock.ohlcv.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OhlcvDto {
    private int open;
    private int high;
    private int low;
    private int close;
    private int volume;
    private LocalDate date;
}
