package com.softsquared.template.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostPurchaseReq {
    private long productId;
    private List<String> options;
    private List<Integer> num;

    private long desId;
    private long paymentCode;

    private int totalPrice;
}
