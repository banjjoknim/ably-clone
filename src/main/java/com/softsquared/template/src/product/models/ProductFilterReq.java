package com.softsquared.template.src.product.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductFilterReq {

    private Long categoryId;
    private Long detailCategoryId;

}
