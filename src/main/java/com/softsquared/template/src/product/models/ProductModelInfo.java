package com.softsquared.template.src.product.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductModelInfo {

    private String modelImage;
    private String modelName;
    private Integer tall;
    private Integer topSize;
    private Integer bottomSize;
    private Integer shoeSize;

    @QueryProjection
    public ProductModelInfo(String modelImage, String modelName, Integer tall, Integer topSize, Integer bottomSize, Integer shoeSize) {
        this.modelImage = modelImage;
        this.modelName = modelName;
        this.tall = tall;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.shoeSize = shoeSize;
    }
}
