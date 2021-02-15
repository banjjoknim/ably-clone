package com.softsquared.template.src.market.models;

import com.softsquared.template.DBmodel.Market;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostMarketReq {

    private String marketName;
    private String image;
    private String instagram;
    private Market.DeliveryType deliveryType;
    private Market.MarketType marketType;
}
