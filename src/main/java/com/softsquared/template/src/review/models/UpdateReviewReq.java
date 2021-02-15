package com.softsquared.template.src.review.models;

import com.softsquared.template.config.statusEnum.ColorComment;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.config.statusEnum.SizeComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateReviewReq {

    private String comment;
    private Satisfaction satisfaction;
    private String purchasedOption;
    private String form;
    private SizeComment sizeComment;
    private ColorComment colorComment;
}
