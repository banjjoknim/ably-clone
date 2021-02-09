package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductMainInfo {

    private Long productId;
    private String productName;
    private Integer discountRate;
    private Integer discountedPrice;
    private Integer originalPrice;
    private String productCode;

    @QueryProjection
    public ProductMainInfo(Long productId, String productName, Integer discountRate, Integer discountedPrice, Integer originalPrice, String productCode) {
        this.productId = productId;
        this.productName = productName;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
        this.productCode = productCode;
    }
}
