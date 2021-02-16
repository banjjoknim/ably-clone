package com.softsquared.template.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostPurchaseRes {
    private long purId;
    private String name;
    private String address;
    private String phoneNum;
}
