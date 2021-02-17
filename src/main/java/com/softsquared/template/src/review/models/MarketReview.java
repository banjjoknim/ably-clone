package com.softsquared.template.src.review.models;

import com.querydsl.core.annotations.QueryProjection;
import com.softsquared.template.config.statusEnum.Satisfaction;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MarketReview {

    private Long productId;
    private String productThumbnail;
    private String productName;
    private Long purchasedCount;
    private Satisfaction satisfaction;
    private String userName;
    private String userRank;
    private String reviewCreatedDate;
    private String reviewComment;

    @QueryProjection
    public MarketReview(Long productId, String productName, Long purchasedCount, Satisfaction satisfaction, String userName, String userRank, String reviewCreatedDate, String reviewComment) {
        this.productId = productId;
        this.productName = productName;
        this.purchasedCount = purchasedCount;
        this.satisfaction = satisfaction;
        this.userName = userName;
        this.userRank = userRank;
        this.reviewCreatedDate = reviewCreatedDate;
        this.reviewComment = reviewComment;
    }
}
