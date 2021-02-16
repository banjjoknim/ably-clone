package com.softsquared.template.src.purchase.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetPurchaseProductReq {
    private long productId;
    private List<String> options;
    private List<Integer> num;


}
