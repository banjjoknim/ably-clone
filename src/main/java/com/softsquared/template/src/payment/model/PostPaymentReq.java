package com.softsquared.template.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostPaymentReq {
    private String uid;
    private int price;

}
