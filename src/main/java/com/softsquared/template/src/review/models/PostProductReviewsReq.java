package com.softsquared.template.src.review.models;


import com.softsquared.template.DBmodel.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostProductReviewsReq {

    private Review.Satisfaction satisfaction;
    private String purchasedOptions;
    private String form;
    private Review.SizeComment sizeComment;
    private Review.ColorComment colorComment;
    private String comment;
}
