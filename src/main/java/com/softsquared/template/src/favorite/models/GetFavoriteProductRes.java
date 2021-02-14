package com.softsquared.template.src.favorite.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFavoriteProductRes {

    private String thumbnail;
    private Long productId;
    private Integer discountRate;
    private Integer discountedPrice;
    private String productName;

    @QueryProjection
    public GetFavoriteProductRes(Long productId, Integer discountRate, Integer discountedPrice, String productName) {
        this.productId = productId;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
        this.productName = productName;
    }
}
