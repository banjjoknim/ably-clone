package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUsersPurchaseProductRes {
    private long prodId;
    private String purState;
    private int price;
    private String prodName;
    private String option;
}
