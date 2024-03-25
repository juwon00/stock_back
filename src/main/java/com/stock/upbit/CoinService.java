package com.stock.upbit;

import com.stock.upbit.dto.CoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinService {

    private final CoinRepository coinRepository;

    public List<CoinDTO> getCoins() {
        List<Coin> coins = coinRepository.findAll();
        return coins.stream().map(this::convertToDto).toList();
    }

    private CoinDTO convertToDto(Coin coin) {
        CoinDTO coinDTO = new CoinDTO();
        BeanUtils.copyProperties(coin, coinDTO);
        return coinDTO;
    }


}
