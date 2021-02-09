package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class GetUserPurchaseProduct {
    private long prodId;
    private char purState;
    private int price;
    private String prodName;
    private String option;

}
