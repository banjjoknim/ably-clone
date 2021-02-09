package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductsInfo {

    private Long productId;
    private Integer discountRate;
    private Integer price;
    private String marketName;
    private String productName;
    private Long purchaseCount;
    private Long reviewCount;

    @QueryProjection
    public ProductsInfo(Long productId, Integer discountRate, Integer price, String marketName, String productName, Long purchaseCount, Long reviewCount) {
        this.productId = productId;
        this.discountRate = discountRate;
        this.price = price;
        this.marketName = marketName;
        this.productName = productName;
        this.purchaseCount = purchaseCount;
        this.reviewCount = reviewCount;
    }
}
