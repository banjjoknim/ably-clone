package com.softsquared.template.src.review;

import com.softsquared.template.DBmodel.FavoriteReviewId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.favorite.FavoriteReviewService;
import com.softsquared.template.src.review.models.PatchReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final FavoriteReviewService favoriteReviewService;

    @Autowired
    public ReviewController(ReviewService reviewService, FavoriteReviewService favoriteReviewService) {
        this.reviewService = reviewService;
        this.favoriteReviewService = favoriteReviewService;
    }

    @PatchMapping("/{reviewId}")
    public BaseResponse<Long> patchReview(@PathVariable Long reviewId, @RequestBody PatchReviewReq request) {
        try {
            if (request.getComment() == null) {
                throw new BaseException(COMMENT_CAN_NOT_BE_EMPTY);
            }
            if (request.getSatisfaction() == null) {
                throw new BaseException(SATISFACTION_CAN_NOT_BE_EMPTY);
            }
            if (request.getSizeComment() == null) {
                throw new BaseException(SIZE_COMMENT_CAN_NOT_BE_EMPTY);
            }
            if (request.getColorComment() == null) {
                throw new BaseException(COLOR_COMMENT_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(SUCCESS, reviewService.updateReview(reviewId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{reviewId}")
    public BaseResponse<Long> deleteReview(@PathVariable Long reviewId) {
        try {
            return new BaseResponse<>(SUCCESS, reviewService.deleteReview(reviewId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{reviewId}/favorite")
    public BaseResponse<FavoriteReviewId> patchProductFavorite(@PathVariable(value = "reviewId") Long reviewId) {
        try {
            return new BaseResponse<>(SUCCESS, favoriteReviewService.updateReviewFavorite(reviewId));
        } catch (BaseException e) {
            if (e.getStatus().equals(EMPTY_JWT)) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            if (e.getStatus().equals(NOT_FOUND_REVIEW)) {
                return new BaseResponse<>(NOT_FOUND_REVIEW);
            }
            return new BaseResponse<>(NOT_FOUND_USERS);
        }
    }
}
