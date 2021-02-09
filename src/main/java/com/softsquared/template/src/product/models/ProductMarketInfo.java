package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductMarketInfo {

    private Long marketId;
    private String marketImage;
    private String marketName;

    @QueryProjection
    public ProductMarketInfo(Long marketId, String marketImage, String marketName) {
        this.marketId = marketId;
        this.marketImage = marketImage;
        this.marketName = marketName;
    }
}
