package com.softsquared.template.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPurchaseProductCasted {
    private String img;
    private String marketName;
    private String productName;
    private int price;
    private int count;
    private String option;
}
