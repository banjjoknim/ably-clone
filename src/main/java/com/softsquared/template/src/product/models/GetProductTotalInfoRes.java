package com.softsquared.template.src.product.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetProductTotalInfoRes {

    private ProductMainInfos productMainInfo;
    private ProductSubInfo productSubInfo;
    private ProductMarketInfos productMarketInfo;
    private ProductDetailInfos productDetailInfo;
    private Long productCountInBasket;
    private Boolean liked;
    private Boolean isSoldOut;

}
