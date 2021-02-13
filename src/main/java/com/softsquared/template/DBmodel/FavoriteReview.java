package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "FavoriteReview")
public class FavoriteReview extends BaseEntity {

    @EmbeddedId
    private FavoriteReviewId favoriteReviewId;

    @Column(name = "liked")
    @Enumerated(EnumType.STRING)
    private FavoriteProduct.Liked liked;

    @Builder
    public FavoriteReview(FavoriteReviewId favoriteReviewId, FavoriteProduct.Liked liked) {
        this.favoriteReviewId = favoriteReviewId;
        this.liked = liked;
    }
}
