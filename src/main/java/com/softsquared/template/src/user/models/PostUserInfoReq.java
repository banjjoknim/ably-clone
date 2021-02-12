package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserInfoReq {

    private String nickname;
    private String email;
    private String age;
    private String gender;

    private String phoneNum;
    private int birthday;
}
