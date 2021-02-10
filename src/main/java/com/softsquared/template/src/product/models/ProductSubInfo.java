package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import com.softsquared.template.DBmodel.Market;
import lombok.Getter;

@Getter
public class ProductSubInfo {

    private Long reviewCount;
    private Long purchaseCount;
    private Integer satisfactionRate;
    private Market.DeliveryType deliveryType;

    @QueryProjection
    public ProductSubInfo(Long reviewCount, Long purchaseCount, Integer satisfactionRate, Market.DeliveryType deliveryType) {
        this.reviewCount = reviewCount;
        this.purchaseCount = purchaseCount;
        this.satisfactionRate = satisfactionRate;
        this.deliveryType = deliveryType;
    }
}
