package com.softsquared.template.src.product.models;

import com.softsquared.template.config.statusEnum.IsOnSale;
import com.softsquared.template.config.statusEnum.IsPublic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProductReq {

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
    private IsOnSale isOnSale;
    private IsPublic isPublic;
}
