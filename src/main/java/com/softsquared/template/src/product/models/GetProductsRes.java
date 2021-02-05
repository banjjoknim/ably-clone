package com.softsquared.template.src.product.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetProductsRes {

    private ProductsInfo productInfo;
    private List<String> thumbnails;
    private Boolean liked;
}
