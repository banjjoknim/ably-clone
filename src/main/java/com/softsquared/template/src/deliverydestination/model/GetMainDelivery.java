package com.softsquared.template.src.deliverydestination.model;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMainDelivery {
    private String userName;
    private String mainAddress;
    private String subAddress;
    private String phoneNum;

    private long mainDel;
}

