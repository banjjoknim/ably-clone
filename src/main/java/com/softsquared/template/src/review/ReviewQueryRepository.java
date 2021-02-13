package com.softsquared.template.src.review;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Review;
import com.softsquared.template.src.review.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QReview.review;
import static com.softsquared.template.DBmodel.QReviewImage.reviewImage;
import static com.softsquared.template.DBmodel.QUserInfo.userInfo;
import static com.softsquared.template.config.Constant.HUNDRED;

@Repository
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ReviewQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<ReviewInfo> getReviewInfos(Long productId) {
        return jpaQueryFactory
                .select(new QReviewInfo(
                        review.id,
                        review.satisfaction.stringValue(),
                        userInfo.userName,
                        userInfo.userRank,
                        Expressions.stringTemplate("DATE_FORMAT({0}, {1})", review.dateCreated, "%Y-%m-%d"),
                        review.likesCount,
                        review.purchasedOption,
                        review.form,
                        review.sizeComment.stringValue(),
                        review.colorComment.stringValue(),
                        review.comment
                ))
                .from(review)
                .innerJoin(userInfo).on(review.userId.eq(userInfo.userCode))
                .where(review.productId.eq(productId))
                .fetch();
    }

    public List<ReviewImageInfo> getReviewImageInfos(Long reviewId) {
        return jpaQueryFactory
                .select(new QReviewImageInfo(reviewImage.reviewId, reviewImage.image))
                .from(reviewImage)
                .where(reviewImage.reviewId.eq(reviewId))
                .fetch();
    }

    public ReviewSummary getReviewSummary(Long productId) {
        return jpaQueryFactory
                .select(new QReviewSummary(
                        review.count(),
                        calculatePercentAboutReview(
                                Expressions.asNumber(JPAExpressions
                                        .select(ExpressionUtils.count(review))
                                        .from(review)
                                        .where(review.satisfaction.eq(Review.Satisfaction.GOOD)
                                                .and(review.productId.eq(productId))))),
                        calculatePercentAboutReview(
                                Expressions.asNumber(JPAExpressions
                                        .select(ExpressionUtils.count(review))
                                        .from(review)
                                        .where(review.colorComment.eq(Review.ColorComment.SCREEN_SAME)
                                                .and(review.productId.eq(productId))))),
                        calculatePercentAboutReview(
                                Expressions.asNumber(JPAExpressions
                                        .select(ExpressionUtils.count(review))
                                        .from(review)
                                        .where(review.sizeComment.eq(Review.SizeComment.FIT)
                                                .and(review.productId.eq(productId)))))
                ))
                .from(review)
                .where(review.productId.eq(productId))
                .fetchFirst();
    }

    public List<ReviewImageInfo> getReviewImageInfosByProductId(Long productId) {
        return jpaQueryFactory
                .select(new QReviewImageInfo(reviewImage.reviewId, reviewImage.image))
                .from(reviewImage)
                .innerJoin(review).on(review.id.eq(reviewImage.reviewId))
                .innerJoin(product).on(product.id.eq(review.productId))
                .where(product.id.eq(productId))
                .fetch();
    }

    private NumberExpression<Integer> calculatePercentAboutReview(NumberExpression expressions) {
        return expressions
                .divide(review.count())
                .multiply(HUNDRED)
                .intValue();
    }
}
