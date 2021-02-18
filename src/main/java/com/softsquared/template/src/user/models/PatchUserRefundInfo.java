package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRefundInfo {
    private String userName;
    private String phoneNum;
    private int point;
    private int coupon;
    private String userRank;
    private String dateCreated;

}
