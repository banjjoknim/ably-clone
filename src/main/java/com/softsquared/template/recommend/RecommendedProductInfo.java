package com.softsquared.template.recommend;

import com.softsquared.template.DBmodel.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecommendedProductInfo implements Comparator<RecommendedProductInfo> {

    private Product product;
    private double similarityScore;

    @Override
    public int compare(RecommendedProductInfo o1, RecommendedProductInfo o2) {
        if (o1.similarityScore >= o2.similarityScore) {
            return 1;
        }
        return -1;
    }
}
