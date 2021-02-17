package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductModelInfo {

    private String modelImage;
    private String modelName;
    private Integer tall;
    private String topSize;
    private String bottomSize;
    private Integer shoeSize;

    @QueryProjection
    public ProductModelInfo(String modelImage, String modelName, Integer tall, String topSize, String bottomSize, Integer shoeSize) {
        this.modelImage = modelImage;
        this.modelName = modelName;
        this.tall = tall;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.shoeSize = shoeSize;
    }
}
