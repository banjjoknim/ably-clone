package com.softsquared.template.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PostLoginReq {
    private String email;
    private String password;
}
