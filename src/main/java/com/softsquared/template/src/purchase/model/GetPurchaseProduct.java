package com.softsquared.template.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPurchaseProduct {
    private String img;
    private String marketName;
    private String productName;
    private int price;
    private int discount;

}
