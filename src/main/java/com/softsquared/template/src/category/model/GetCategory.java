package com.softsquared.template.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategory {
    private long categoryCode;
    private String categoryName;
    private String dateCreated;
    private String dateUpdated;
}
