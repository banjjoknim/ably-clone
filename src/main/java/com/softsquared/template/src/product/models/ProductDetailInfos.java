package com.softsquared.template.src.product.models;

import com.softsquared.template.DBmodel.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProductDetailInfos {

    private List<String> productDetailImages;
    private ProductModelInfo productModelInfo;
    private ProductDetail productDetail;

}
