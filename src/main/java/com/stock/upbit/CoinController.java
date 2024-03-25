package com.stock.upbit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.stock.upbit.dto.CoinDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/upbit")
@RequiredArgsConstructor
@Slf4j
public class CoinController {

    @Value("${upbit.access-key}")
    String accessKey;

    @Value("${upbit.secret-key}")
    String secretKey;
    String serverUrl = "https://api.upbit.com/v1";

    private final CoinService coinService;

    // 전체 계좌 조회
    @GetMapping("/get")
    public void getAccounts() {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create().withClaim("access_key", accessKey).withClaim("nonce", UUID.randomUUID().toString()).sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println(EntityUtils.toString(entity, "UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/coins")
    public List<CoinDTO> getCoins() {
        return coinService.getCoins();
    }

    @GetMapping("/coins/back-test")
    public String getCoinBackTest() {
        return "1";
    }
}
