package com.softsquared.template.src.product.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterReq {

    private Long categoryId;
    private Long detailCategoryId;
    private Integer minimumPrice;
    private Integer maximumPrice;
    private Long[] colorIds;
    private Long[] printIds;
    private Long[] fabricIds;
    private Integer minimumTall;
    private Integer maximumTall;
    private Long[] ageGroupIds;
    private Long[] clothLengthIds;

}
