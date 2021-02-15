package com.softsquared.template.src.favorite;

import com.softsquared.template.DBmodel.FavoriteReview;
import com.softsquared.template.DBmodel.FavoriteReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FavoriteReviewRepository extends JpaRepository<FavoriteReview, Long> {

    @Query(value = "select fr from FavoriteReview fr where fr.favoriteReviewId = :favoriteReviewId")
    Optional<FavoriteReview> findByFavoriteReviewId(@Param("favoriteReviewId") FavoriteReviewId favoriteReviewId);

    @Transactional
    @Modifying
    @Query(value = "update FavoriteReview fr set fr.liked = 'YES' where fr.favoriteReviewId.userId = :userId and fr.favoriteReviewId.reviewId = :reviewId")
    void updateFavoriteIsYes(@Param(value = "userId") Long userId, @Param(value = "reviewId") Long reviewId);

    @Transactional
    @Modifying
    @Query(value = "update FavoriteReview fr set fr.liked = 'NO' where fr.favoriteReviewId.userId = :userId and fr.favoriteReviewId.reviewId = :reviewId")
    void updateFavoriteIsNo(@Param(value = "userId") Long userId, @Param(value = "reviewId") Long reviewId);
}
