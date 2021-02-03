package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginRes {
    private final int userId;
    private final String jwt;
}
