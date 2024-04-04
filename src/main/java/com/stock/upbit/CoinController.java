package com.stock.upbit;

import com.stock.status.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/upbit")
@RequiredArgsConstructor
@Slf4j
public class CoinController {


    private final CoinService coinService;

    // 주문 - 매수
    @GetMapping("/order/buy")
    public ResponseEntity<Message> buy() throws NoSuchAlgorithmException {
        String balance = coinService.getBalance();
        coinService.buy(balance);
        return Message.MessagetoResponseEntity("매수 성공");
    }

    // 주문 - 매도
    @GetMapping("/order/sell")
    public ResponseEntity<Message> sell() throws NoSuchAlgorithmException {
        String balance = coinService.getBalance();
        coinService.sell(balance);
        return Message.MessagetoResponseEntity("매도 성공");
    }
}
