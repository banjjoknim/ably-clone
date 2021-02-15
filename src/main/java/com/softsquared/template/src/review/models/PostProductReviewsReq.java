package com.softsquared.template.src.review.models;


import com.softsquared.template.config.statusEnum.ColorComment;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.config.statusEnum.SizeComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostProductReviewsReq {

    private Satisfaction satisfaction;
    private String purchasedOptions;
    private String form;
    private SizeComment sizeComment;
    private ColorComment colorComment;
    private String comment;
}
