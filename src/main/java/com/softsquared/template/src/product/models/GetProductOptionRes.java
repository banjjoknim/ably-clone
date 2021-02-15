package com.softsquared.template.src.product.models;

import com.softsquared.template.DBmodel.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetProductOptionRes {

    private Long optionId;
    private String optionName;
    private ProductOption.OptionRank optionRank;

}
