package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUsersPurchaseProductStatusRes {
    int ready = 0;     //배송중
    int complete = 0;  //배송 완료
    int delete = 0;    //취소,반품
}
