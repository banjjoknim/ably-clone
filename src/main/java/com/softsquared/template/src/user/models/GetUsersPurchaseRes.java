package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetUsersPurchaseRes {
    private GetUsersPurchaseProductStatusRes purchaseStatus;
    private List<GetUsersPurchasesTemp> purchaseList;

}
