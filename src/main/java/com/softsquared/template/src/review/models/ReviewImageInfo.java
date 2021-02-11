package com.softsquared.template.src.review.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ReviewImageInfo {

    private Long reviewId;
    private String reviewImage;

    @QueryProjection
    public ReviewImageInfo(Long reviewId, String reviewImage) {
        this.reviewId = reviewId;
        this.reviewImage = reviewImage;
    }
}
