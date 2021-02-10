package com.softsquared.template.src.product.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetProductRes {

    private Long productCountInBasket;
    private ProductMainInfos productMainInfos;
    private ProductSubInfo productSubInfo;
    private ProductMarketInfos productMarketInfos;
    private ProductDetailInfos productDetailInfos;
    private Boolean liked;
    private Boolean onSale;

}
