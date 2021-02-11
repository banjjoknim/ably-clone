package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import com.softsquared.template.DBmodel.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ProductDetailContent {

    private String content;
    private ProductImage.ImageType type;

    @QueryProjection
    public ProductDetailContent(String content, ProductImage.ImageType type) {
        this.content = content;
        this.type = type;
    }
}
