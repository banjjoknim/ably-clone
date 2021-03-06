package com.softsquared.template.src.model.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PatchModelReq {

    private String modelName;
    private String modelImage;
    private Integer tall;
    private String topSize;
    private String bottomSize;
    private Integer shoeSize;
}
