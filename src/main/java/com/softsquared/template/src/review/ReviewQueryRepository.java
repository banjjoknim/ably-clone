package com.softsquared.template.src.review;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.config.statusEnum.ColorComment;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.config.statusEnum.SizeComment;
import com.softsquared.template.src.review.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.softsquared.template.DBmodel.QMarket.market;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QProductImage.productImage;
import static com.softsquared.template.DBmodel.QPurchase.purchase;
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

    public MarketReviewSummary getMarketReviewSummary(Long marketId, Long categoryId) {
        return jpaQueryFactory
                .select(new QMarketReviewSummary(
                        new CaseBuilder()
                                .when(calculateSatisfactionFromMarketReview(marketId, categoryId).isNull())
                                .then(Expressions.asNumber(0.0))
                                .otherwise(calculateSatisfactionFromMarketReview(marketId, categoryId)),
                        calculateMarketReviewCount(marketId, categoryId)
                ))
                .from(market)
                .where(market.id.eq(marketId))
                .fetchFirst();
    }

    private JPQLQuery<Long> calculateMarketReviewCount(Long marketId, Long categoryId) {
        return JPAExpressions
                .select(review.count())
                .from(review)
                .innerJoin(product).on(review.productId.eq(product.id))
                .where(marketEq(marketId))
                .where(categoryEq(categoryId));
    }

    private NumberExpression<Double> calculateSatisfactionFromMarketReview(Long marketId, Long categoryId) {
        return Expressions.stringTemplate("TRUNCATE({0}, {1})",
                Expressions.asNumber(JPAExpressions.select(review.count()
                        .multiply(HUNDRED)
                        .divide(Expressions.asNumber(calculateMarketReviewCount(marketId, categoryId))))
                        .from(review)
                        .innerJoin(product).on(review.productId.eq(product.id))
                        .where(marketEq(marketId))
                        .where(categoryEq(categoryId))
                        .where(review.satisfaction.eq(Satisfaction.GOOD))),
                REVIEW_SUMMARY_SATISFACTION_TRUNCATE_POSITION)
                .castToNum(Double.class);
    }

    private BooleanExpression categoryEq(Long categoryId) {
        if (categoryId != null) {
            return product.categoryId.eq(categoryId);
        }
        return null;
    }

    private BooleanExpression marketEq(Long marketId) {
        return product.marketId.eq(marketId);
    }

    public List<MarketReview> getMarketReviews(Long marketId, Long categoryId) {
        return jpaQueryFactory
                .select(new QMarketReview(
                        product.id,
                        product.name,
                        JPAExpressions
                                .select(count(purchase))
                                .from(purchase)
                                .where(purchase.purProductCode.eq(product.id)),
                        review.satisfaction,
                        userInfo.userName,
                        userInfo.userRank,
                        Expressions.stringTemplate("DATE_FORMAT({0}, {1})", review.dateCreated, "%Y-%m-%d"),
                        review.comment
                ))
                .from(product)
                .innerJoin(review).on(product.id.eq(review.productId))
                .innerJoin(userInfo).on(review.userId.eq(userInfo.userId))
                .where(marketEq(marketId).and(categoryEq(categoryId)))
                .fetch();
    }

    public String getMarketReviewImage(Long marketId) {
        return jpaQueryFactory
                .select(productImage.image)
                .from(productImage)
                .innerJoin(product).on(productImage.productId.eq(product.id))
                .where(product.marketId.eq(marketId))
                .where(productImage.type.eq(ProductImage.ImageType.THUMBNAIL))
                .fetchFirst();
    }

    /**
     * 안뇽하세요 콜트님
     * 회원의 리뷰수좀 빼가겠습니당
     */
    public long findReviewCountByUserId(long userId) {
        // QReview review = QReview.review;
        return jpaQueryFactory.select(review.count())
                .from(review)
                .where(review.userId.eq(userId))
                .fetchCount();
    }
}
