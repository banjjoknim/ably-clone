package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserInfo {
    private String userName;
    private String email;
    private int status;
}
