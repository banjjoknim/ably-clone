package com.softsquared.template.src.review;

import com.softsquared.template.DBmodel.Review;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.product.ProductRepository;
import com.softsquared.template.src.review.models.PostProductReviewsReq;
import com.softsquared.template.src.user.UserInfoRepository;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    private final UserInfoRepository userInfoRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;

    @Autowired
    public ReviewService(UserInfoRepository userInfoRepository, ProductRepository productRepository, ReviewRepository reviewRepository, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.jwtService = jwtService;
    }

    public Long createProductReviews(Long productId, PostProductReviewsReq request) throws BaseException {
        Long userId = jwtService.getUserId();
        if (!userInfoRepository.existsById(userId)) {
            throw new BaseException(NOT_FOUND_USERS);
        }
        if (!productRepository.existsById(productId)) {
            throw new BaseException(NOT_FOUND_PRODUCT);
        }

        Review review = Review.builder()
                .userId(userId)
                .productId(productId)
                .satisfaction(request.getSatisfaction())
                .purchasedOption(request.getPurchasedOptions())
                .form(request.getForm())
                .sizeComment(request.getSizeComment())
                .colorComment(request.getColorComment())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);

        return review.getId();
    }
}
