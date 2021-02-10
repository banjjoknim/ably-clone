package com.softsquared.template.src.deliverydestination.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserDeliveryRes {
    private String userName;
    private String mainAddress;
    private String subAddress;
    private String phoneNum;
    private boolean isMain;
}
