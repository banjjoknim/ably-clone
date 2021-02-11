package com.softsquared.template.src.review.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewInfo {

    private Long reviewId;
    private String satisfaction;
    private String userName;
    private String rank;
    private String dateCreated;
    private Long likedCount;
    private String purchasedOptions;
    private List<String> reviewPictures;
    private String form; // 체형
    private String sizeComment; //
    private String colorComment;
    private String reviewComment;

    @QueryProjection
    public ReviewInfo(Long reviewId, String satisfaction, String userName, String rank, String dateCreated, Long likedCount, String purchasedOptions, String form, String sizeComment, String colorComment, String reviewComment) {
        this.reviewId = reviewId;
        this.satisfaction = satisfaction;
        this.userName = userName;
        this.rank = rank;
        this.dateCreated = dateCreated;
        this.likedCount = likedCount;
        this.purchasedOptions = purchasedOptions;
        this.form = form;
        this.sizeComment = sizeComment;
        this.colorComment = colorComment;
        this.reviewComment = reviewComment;
    }

    public ReviewInfo getReviewWithPictures(List<String> reviewPictures) {
        this.reviewPictures = reviewPictures;
        return this;
    }
}
