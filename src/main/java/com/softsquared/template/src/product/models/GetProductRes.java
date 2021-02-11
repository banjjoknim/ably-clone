package com.softsquared.template.src.product.models;


import com.softsquared.template.src.review.models.ProductReviews;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetProductRes {

    private Long productCountInBasket;
    private ProductMainInfos productMainInfos;
    private ProductSubInfos productSubInfos;
    private ProductMarketInfos productMarketInfos;
    private ProductDetailInfos productDetailInfos;
    private ProductReviews productReviews;
    private Boolean liked;
    private Boolean onSale;

}
