package com.softsquared.template.src.product.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostProductImageReq {

    private List<ProductDetailContent> productImages;
}
