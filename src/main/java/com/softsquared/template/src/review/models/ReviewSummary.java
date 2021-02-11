package com.softsquared.template.src.review.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewSummary {

    private Long reviewCount;
    private Integer satisfactionRate;
    private Integer colorEvaluationRate;
    private Integer fitEvaluationRate;
    private List<ReviewImageInfo> reviewImageInfos;

    @QueryProjection
    public ReviewSummary(Long reviewCount, Integer satisfactionRate, Integer colorEvaluationRate, Integer fitEvaluationRate) {
        this.reviewCount = reviewCount;
        this.satisfactionRate = satisfactionRate;
        this.colorEvaluationRate = colorEvaluationRate;
        this.fitEvaluationRate = fitEvaluationRate;
    }

    public ReviewSummary getReviewSummaryWithImageInfos(List<ReviewImageInfo> reviewImageInfos) {
        this.reviewImageInfos = reviewImageInfos;
        return this;
    }
}
