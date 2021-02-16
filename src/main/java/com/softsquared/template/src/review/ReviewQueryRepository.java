package com.softsquared.template.src.review;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.config.statusEnum.ColorComment;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.config.statusEnum.SizeComment;
import com.softsquared.template.src.review.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.QMarket.market;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QReview.review;
import static com.softsquared.template.DBmodel.QReviewImage.reviewImage;
import static com.softsquared.template.DBmodel.QUserInfo.userInfo;
import static com.softsquared.template.config.Constant.HUNDRED;
import static com.softsquared.template.config.Constant.REVIEW_SUMMARY_SATISFACTION_TRUNCATE_POSITION;

@Repository
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ReviewQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<ReviewInfo> getReviewInfos(Long productId, PageRequest pageable) {
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
                .innerJoin(userInfo).on(review.userId.eq(userInfo.userId))
                .where(review.productId.eq(productId))
                .orderBy(review.dateCreated.desc())
                .offset(pageable.getPage() * pageable.getSize())
                .limit(pageable.getSize())
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
                                        .where(review.satisfaction.eq(Satisfaction.GOOD)
                                                .and(review.productId.eq(productId))))),
                        calculatePercentAboutReview(
                                Expressions.asNumber(JPAExpressions
                                        .select(ExpressionUtils.count(review))
                                        .from(review)
                                        .where(review.colorComment.eq(ColorComment.SCREEN_SAME)
                                                .and(review.productId.eq(productId))))),
                        calculatePercentAboutReview(
                                Expressions.asNumber(JPAExpressions
                                        .select(ExpressionUtils.count(review))
                                        .from(review)
                                        .where(review.sizeComment.eq(SizeComment.FIT)
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

    public MarketReviewSummary getMarketReviews(Long marketId) {
        return jpaQueryFactory
                .select(new QMarketReviewSummary(
                        Expressions.stringTemplate("TRUNCATE({0}, {1})",
                                Expressions.asNumber(JPAExpressions.select(review.count()
                                        .multiply(HUNDRED)
                                        .divide(Expressions.asNumber(calculateMarketReviewCount(marketId))))
                                        .from(review)
                                        .innerJoin(product).on(review.productId.eq(product.id))
                                        .where(review.satisfaction.eq(Satisfaction.GOOD).and(product.marketId.eq(marketId)))),
                                REVIEW_SUMMARY_SATISFACTION_TRUNCATE_POSITION)
                                .castToNum(Double.class),
                        JPAExpressions
                                .select(review.count())
                                .from(review)
                                .innerJoin(product).on(review.productId.eq(product.id))
                                .where(product.marketId.eq(marketId))
                ))
                .from(market)
                .where(market.id.eq(marketId))
                .fetchFirst();
    }

    private JPQLQuery<Long> calculateMarketReviewCount(Long marketId) {
        return JPAExpressions
                .select(review.count())
                .from(review)
                .innerJoin(product).on(review.productId.eq(product.id))
                .where(product.marketId.eq(marketId));
    }
}
