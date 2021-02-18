package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRefunInfoReq {
    private String refundAccount;
    private String refundBank;
    private String refundName;
}
