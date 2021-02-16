package com.softsquared.template.src.market.models;

import com.querydsl.core.annotations.QueryProjection;
import com.softsquared.template.DBmodel.Market;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetMarketMainInfoRes {

    private String marketImage;
    private Market.MarketType marketType;
    private String marketName;
    private String marketTags;
    private Long marketProductCount;
    private Long marketReviewCount;
    private Integer satisfactionRate;
    private Long likedCount;

    @QueryProjection
    public GetMarketMainInfoRes(String marketImage, Market.MarketType marketType, String marketName, Long marketProductCount, Long marketReviewCount, Integer satisfactionRate, Long likedCount) {
        this.marketImage = marketImage;
        this.marketType = marketType;
        this.marketName = marketName;
        this.marketProductCount = marketProductCount;
        this.marketReviewCount = marketReviewCount;
        this.satisfactionRate = satisfactionRate;
        this.likedCount = likedCount;
    }
}
