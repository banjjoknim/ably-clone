package com.softsquared.template.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetPurchaseProductRes {
    private String totalNum;
    private String marketName;
    private List<GetPurchaseProductCasted> productList;
}
