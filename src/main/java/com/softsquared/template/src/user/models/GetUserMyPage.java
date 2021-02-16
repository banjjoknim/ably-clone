package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserMyPage {
    private String name;
    private String rank;
    private int point;
    private int coupon;

}
