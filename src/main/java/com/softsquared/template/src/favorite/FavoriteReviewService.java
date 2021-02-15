package com.softsquared.template.src.favorite;

import com.softsquared.template.DBmodel.FavoriteReview;
import com.softsquared.template.DBmodel.FavoriteReviewId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.statusEnum.Liked;
import com.softsquared.template.src.review.ReviewRepository;
import com.softsquared.template.src.user.UserInfoRepository;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_REVIEW;
import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_USERS;

@Service
public class FavoriteReviewService {

    private final FavoriteReviewRepository favoriteReviewRepository;
    private final ReviewRepository reviewRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;

    @Autowired
    public FavoriteReviewService(FavoriteReviewRepository favoriteReviewRepository, ReviewRepository reviewRepository, UserInfoRepository userInfoRepository, JwtService jwtService) {
        this.favoriteReviewRepository = favoriteReviewRepository;
        this.reviewRepository = reviewRepository;
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
    }

    public FavoriteReviewId updateReviewFavorite(Long reviewId) throws BaseException {
        Long userId = jwtService.getUserId();
        FavoriteReviewId favoriteReviewId = new FavoriteReviewId(userId, reviewId);
        Optional<FavoriteReview> favoriteReview = favoriteReviewRepository.findByFavoriteReviewId(favoriteReviewId);

        if (!userInfoRepository.existsById(userId)) {
            throw new BaseException(NOT_FOUND_USERS);
        }
        if (!reviewRepository.existsById(reviewId)) {
            throw new BaseException(NOT_FOUND_REVIEW);
        }

        if (favoriteReview.isPresent()) {
            if (favoriteReview.get().getLiked().equals(Liked.YES)) {
                favoriteReviewRepository.updateFavoriteIsNo(userId, reviewId);
                reviewRepository.updateReviewCountMinus(reviewId);
                return favoriteReviewId;
            }
            favoriteReviewRepository.updateFavoriteIsYes(userId, reviewId);
            reviewRepository.updateReviewCountPlus(reviewId);
            return favoriteReviewId;
        }
        createReviewFavorite(favoriteReviewId);

        return favoriteReviewId;
    }

    private void createReviewFavorite(FavoriteReviewId favoriteReviewId) {
        FavoriteReview favoriteReview = new FavoriteReview(favoriteReviewId, Liked.YES);
        reviewRepository.updateReviewCountPlus(favoriteReviewId.getReviewId());
        favoriteReviewRepository.save(favoriteReview);
    }
}
