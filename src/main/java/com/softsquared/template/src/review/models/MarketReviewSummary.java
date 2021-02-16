package com.softsquared.template.src.review.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MarketReviewSummary {

    private Double satisfactionRate;
    private Long marketReviewCount;

    @QueryProjection
    public MarketReviewSummary(Double satisfactionRate, Long marketReviewCount) {
        this.satisfactionRate = satisfactionRate;
        this.marketReviewCount = marketReviewCount;
    }
}
