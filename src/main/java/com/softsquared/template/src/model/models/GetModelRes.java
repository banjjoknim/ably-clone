package com.softsquared.template.src.model.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetModelRes {

    private String modelName;
    private String modelImage;
    private Integer tall;
    private String topSize;
    private String bottomSize;
    private Integer shoeSize;
}
