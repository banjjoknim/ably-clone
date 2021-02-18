package com.softsquared.template.recommend;

import com.softsquared.template.DBmodel.Product;

public class Vectorizer {
    private double marketScore;
    private double categoryScore;
    private double detailCategoryScore;
    private double ageGroupScore;
    private double clothLengthScore;
    private double colorScore;
    private double fabricScore;
    private double tallScore;
    private double fitScore;
    private double printScore;

    public double[] getScoreVector(Product lastViewedProduct, Product recommendedProduct) {
        if (lastViewedProduct.getMarketId().equals(recommendedProduct.getMarketId())) {
            marketScore = 1;
        }
        if (lastViewedProduct.getCategoryId().equals(recommendedProduct.getCategoryId())) {
            categoryScore = 1;
        }
        if (lastViewedProduct.getDetailCategoryId().equals(recommendedProduct.getDetailCategoryId())) {
            detailCategoryScore = 1;
        }
        if (lastViewedProduct.getAgeGroupId().equals(recommendedProduct.getAgeGroupId())) {
            ageGroupScore = 1;
        }
        if (lastViewedProduct.getClothLengthId().equals(recommendedProduct.getClothLengthId())) {
            clothLengthScore = 1;
        }
        if (lastViewedProduct.getColorId().equals(recommendedProduct.getColorId())) {
            colorScore = 1;
        }
        if (lastViewedProduct.getFabricId().equals(recommendedProduct.getFabricId())) {
            fabricScore = 1;
        }
        if (lastViewedProduct.getTall() * 0.9 <= recommendedProduct.getTall() &&
                recommendedProduct.getTall() <= lastViewedProduct.getTall() * 1.1) {
            tallScore = 1;
        }
        if (lastViewedProduct.getFitId().equals(recommendedProduct.getFitId())) {
            fitScore = 1;
        }
        if (lastViewedProduct.getPrintId().equals(recommendedProduct.getPrintId())) {
            printScore = 1;
        }
        return new double[]{marketScore, categoryScore, detailCategoryScore,
                ageGroupScore, clothLengthScore, colorScore,
                fabricScore, tallScore, fitScore, printScore};
    }
}
