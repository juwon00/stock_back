package com.stock.stocks;

import com.stock.ohlcv.kospi.KospiOhlcv;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class KoreaStocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "korea_stocks_id")
    private Long id;

    private String name;

    private String code;

    private String market;

    private boolean delisting;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "koreaStocks")
    private List<KospiOhlcv> kospiOhlcvs = new ArrayList<>();

    public KoreaStocks(String code, String name, String market) {
        this.name = name;
        this.code = code;
        this.market = market;
    }
}
