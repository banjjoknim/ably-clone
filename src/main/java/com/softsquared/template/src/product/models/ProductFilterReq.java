package com.softsquared.template.src.product.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductFilterReq {

    private Long categoryId;
    private Long detailCategoryId;
    private Integer minimumPrice;
    private Integer maximumPrice;
    private List<Long> colorIds;
    private List<Long> printIds;
    private List<Long> fabricIds;
    private Integer minimumTall;
    private Integer maximumTall;
    private List<Long> ageGroupIds;
    private List<Long> clothLengthIds;

}
