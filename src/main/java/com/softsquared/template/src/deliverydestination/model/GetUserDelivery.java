package com.softsquared.template.src.deliverydestination.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class GetUserDelivery {
    private String userName;
    private String mainAddress;
    private String subAddress;
    private String phoneNum;
    private int isMain;

}
