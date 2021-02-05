package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductsInfo {

    private Long productId;
    private Integer discountRate;
    private Integer price;
    private String celebName;
    private String productName;
    private Long purchaseCount;

    @QueryProjection
    public ProductsInfo(Long productId, Integer discountRate, Integer price, String celebName, String productName, Long purchaseCount) {
        this.productId = productId;
        this.discountRate = discountRate;
        this.price = price;
        this.celebName = celebName;
        this.productName = productName;
        this.purchaseCount = purchaseCount;
    }
}
