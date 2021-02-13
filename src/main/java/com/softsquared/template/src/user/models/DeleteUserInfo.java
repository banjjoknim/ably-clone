package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@Getter
public class DeleteUserInfo {
    private String email;
    private String userName;
    private String phoneNum;
    private int birthday;
    private String height;
    private String weight;
    private String topSize;
    private String bottomSize;
    private String shoeSize;
    private String refundBank;
    private String refundName;
    private String refundAccount;
    private int point;
    private int coupon;
    private String userRank;
    private String dateUpdated;
    private String dateCreated;
    private String gender;
    private String age;
}

