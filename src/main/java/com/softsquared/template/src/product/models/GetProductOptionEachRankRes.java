package com.softsquared.template.src.product.models;

import com.softsquared.template.DBmodel.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetProductOptionEachRankRes {

    private List<ProductOption> firstOptions;
    private List<ProductOption> secondOptions;

}
