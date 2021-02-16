package com.softsquared.template.src.deliverydestination.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDeliveryReq {
    private String name;
    private String address;
    private String detailAddress;
    private String phoneNum;

}
