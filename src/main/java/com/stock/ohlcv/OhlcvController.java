package com.stock.ohlcv;

import com.stock.ohlcv.dto.OhlcvDto;
import com.stock.status.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ohlcv")
@RequiredArgsConstructor
public class OhlcvController {

    private final OhlcvService ohlcvService;

    @GetMapping()
    public ResponseEntity<Message> getStocks(@RequestParam("value") String value) {

        List<OhlcvDto> stocks = ohlcvService.getStocks(value);

        return Message.MessagetoResponseEntity(stocks);
    }
}
