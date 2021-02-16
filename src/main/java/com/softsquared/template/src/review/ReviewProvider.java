package com.softsquared.template.src.review;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.src.product.ProductRepository;
import com.softsquared.template.src.review.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ReviewProvider {

    private final ReviewQueryRepository reviewQueryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewProvider(ReviewQueryRepository reviewQueryRepository, ProductRepository productRepository) {
        this.reviewQueryRepository = reviewQueryRepository;
        this.productRepository = productRepository;
    }

    public GetProductReviewsRes retrieveProductReviews(Long productId, PageRequest pageable) throws BaseException {
        if (!productRepository.existsById(productId)) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_PRODUCT);
        }
        ReviewSummary reviewSummary = reviewQueryRepository.getReviewSummary(productId)
                .getReviewSummaryWithImageInfos(reviewQueryRepository.getReviewImageInfosByProductId(productId));
        List<ReviewInfo> reviewInfos = getProductReviewsWithImages(productId, pageable);

        return new GetProductReviewsRes(reviewSummary, reviewInfos);
    }

    private List<ReviewInfo> getProductReviewsWithImages(Long productId, PageRequest pageable) {
        List<ReviewInfo> reviewInfos = reviewQueryRepository.getReviewInfos(productId, pageable);

        return reviewInfos.stream()
                .map(reviewInfo -> {
                    List<ReviewImageInfo> reviewImageInfos = reviewQueryRepository
                            .getReviewImageInfos(reviewInfo.getReviewId());
                    return reviewInfo.getReviewWithPictures(reviewImageInfos);
                })
                .collect(toList());
    }

    public GetMarketReviewRes retrieveMarketReview(Long marketId) {
        MarketReviewSummary marketReviewSummary = reviewQueryRepository.getMarketReviews(marketId);
        return new GetMarketReviewRes(marketReviewSummary, null);
    }

}
