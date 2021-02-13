package com.softsquared.template.src.review;

import com.softsquared.template.DBmodel.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Review r set r.likesCount = r.likesCount + 1 where r.id = :reviewId")
    void updateReviewCountPlus(@Param(value = "reviewId") Long reviewId);

    @Transactional
    @Modifying
    @Query(value = "update Review r set r.likesCount = r.likesCount - 1 where r.id = :reviewId")
    void updateReviewCountMinus(@Param(value = "reviewId") Long reviewId);
}
