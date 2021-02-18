package com.softsquared.template.recommend;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class VectorCalculator {

    private final double[] defaultProductScoreVector = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final double[] compareProductScoreVector;

    public VectorCalculator(double[] compareProductScoreVector) {
        this.compareProductScoreVector = compareProductScoreVector;
    }

    // 두 상품 사이의 코사인 유사도 연산
    public double calculateCosineSimilarity() {
        double scoreVectorSize1 = calculateVectorSize(defaultProductScoreVector);
        double scoreVectorSize2 = calculateVectorSize(compareProductScoreVector);
        double innerProductFromScoreVectors = calculateInnerProductFromTwoScoreVector();

        return innerProductFromScoreVectors / (scoreVectorSize1 * scoreVectorSize2);
    }

    // 가중치가 적용된 상품 속성 벡터의 크기 계산
    private double calculateVectorSize(double[] scoreVector) {
        return Math.sqrt(Arrays.stream(scoreVector)
                .map(score -> Math.pow(score, 2))
                .reduce(0, (x, y) -> x + y));
    }

    // 두 상품 벡터 각도가 같을 때 내적 계산
    private double calculateInnerProductFromTwoScoreVector() {
        return DoubleStream.iterate(0, x -> x + 1)
                .limit(compareProductScoreVector.length)
                .mapToInt(index -> (int) index)
                .mapToDouble(index -> Math.abs(defaultProductScoreVector[index]) * Math.abs(compareProductScoreVector[index]))
                .reduce(0, (x, y) -> x + y);
    }

}
