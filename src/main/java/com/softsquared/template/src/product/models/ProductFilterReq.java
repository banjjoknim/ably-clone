package com.softsquared.template.src.product.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ProductFilterReq {

    private Optional<Long> categoryId;
    private Optional<Long> detailCategoryId;
    private Optional<Integer> minimumPrice;
    private Optional<Integer> maximumPrice;
    private Optional<List<Long>> colorIds;
    private Optional<List<Long>> printIds;
    private Optional<List<Long>> fabricIds;
    private Optional<Integer> minimumTall;
    private Optional<Integer> maximumTall;
    private Optional<List<Long>> ageGroupIds;
    private Optional<List<Long>> clothLengthIds;

    @Builder
    public ProductFilterReq(Optional<Long> categoryId, Optional<Long> detailCategoryId, Optional<Integer> minimumPrice, Optional<Integer> maximumPrice, Optional<List<Long>> colorIds, Optional<List<Long>> printIds, Optional<List<Long>> fabricIds, Optional<Integer> minimumTall, Optional<Integer> maximumTall, Optional<List<Long>> ageGroupIds, Optional<List<Long>> clothLengthIds) {
        this.categoryId = categoryId;
        this.detailCategoryId = detailCategoryId;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
        this.colorIds = colorIds;
        this.printIds = printIds;
        this.fabricIds = fabricIds;
        this.minimumTall = minimumTall;
        this.maximumTall = maximumTall;
        this.ageGroupIds = ageGroupIds;
        this.clothLengthIds = clothLengthIds;
    }
}
