package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * GerUsersPurchase에서 갖고온 정보 + 해당 GetUsersPurcahseProdcut
 */
@Getter
@AllArgsConstructor
public class GetUsersPurchasesTemp {
    private long purId;
    private String dateCreated;
    private List<GetUsersPurchaseProductRes> purProductList;
}
