package com.softsquared.template.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostPurchaseReq {
    private String productId;
    private List<String> options;
    private List<Integer> num;
}
