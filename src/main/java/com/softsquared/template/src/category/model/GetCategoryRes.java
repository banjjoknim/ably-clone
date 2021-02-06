package com.softsquared.template.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoryRes {
    private long categoryCode;
    private String categoryName;
    private boolean newStatus;
}
