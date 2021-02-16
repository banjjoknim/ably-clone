package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class GetUserMyPageRes {
    private String name;
    private String rank;
    private int point;
    private int coupon;
    private long purchaseCount;
    private long reviewCount;
}
