package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Embeddable
public class FavoriteReviewId implements Serializable {

    private Long userId;
    private Long reviewId;

    @Builder
    public FavoriteReviewId(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
