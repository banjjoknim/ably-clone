package com.softsquared.template.src.review;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.src.review.models.QReviewImageInfo;
import com.softsquared.template.src.review.models.QReviewInfo;
import com.softsquared.template.src.review.models.ReviewImageInfo;
import com.softsquared.template.src.review.models.ReviewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.QReview.review;
import static com.softsquared.template.DBmodel.QReviewImage.reviewImage;
import static com.softsquared.template.DBmodel.QUserInfo.userInfo;
import static java.util.stream.Collectors.toList;

@Repository
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ReviewQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<ReviewInfo> getProductReviews(Long productId) {
        List<ReviewInfo> reviewInfos = getReviewInfos(productId);
        return reviewInfos.stream()
                .map(reviewInfo -> {
                    List<ReviewImageInfo> reviewImageInfos = getReviewImageInfos(reviewInfo.getReviewId());
                    return reviewInfo.getReviewWithPictures(reviewImageInfos);
                })
                .collect(toList());
    }

    private List<ReviewInfo> getReviewInfos(Long productId) {
        return jpaQueryFactory
                .select(new QReviewInfo(
                        review.id,
                        review.satisfaction.stringValue(),
                        userInfo.userName,
                        userInfo.rank,
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

    private List<ReviewImageInfo> getReviewImageInfos(Long reviewId) {
        return jpaQueryFactory
                .select(new QReviewImageInfo(reviewImage.reviewId, reviewImage.image))
                .from(reviewImage)
                .where(reviewImage.reviewId.eq(reviewId))
                .fetch();
    }

}
