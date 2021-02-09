package com.softsquared.template.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class GetCategory {
    private long categoryCode;
    private String categoryName;
    private Date dateCreated;
    private Date dateUpdated;

}
