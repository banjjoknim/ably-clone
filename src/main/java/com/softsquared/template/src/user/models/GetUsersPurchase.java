package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUsersPurchase {
    private long purId;
    private Date dateCreated;
}
