package com.softsquared.template.src.product.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostProductReq {

    private String productName;
    private Long categoryId;
    private Long detailCategoryId;
    private Long ageGroupId;
    private Long clothLengthId;
    private Long colorId;
    private Long fabricId;
    private Integer tall;
    private Long fitId;
    private Long printId;
    private Long modelId;
    private Integer price;
    private Integer discountRate;
    private List<ProductDetailContent> productImages;
}
