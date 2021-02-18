package com.softsquared.template.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostPaymentReq {
    private String imp_id;
    private int imp_price;

}
