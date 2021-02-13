package com.softsquared.template.src.review;

import com.softsquared.template.DBmodel.FavoriteProductId;
import com.softsquared.template.DBmodel.FavoriteReviewId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.favorite.FavoriteReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final FavoriteReviewService favoriteReviewService;

    @Autowired
    public ReviewController(FavoriteReviewService favoriteReviewService) {
        this.favoriteReviewService = favoriteReviewService;
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
