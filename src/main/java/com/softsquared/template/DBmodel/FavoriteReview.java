package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.statusEnum.Liked;
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
    private Liked liked;

    @Builder
    public FavoriteReview(FavoriteReviewId favoriteReviewId, Liked liked) {
        this.favoriteReviewId = favoriteReviewId;
        this.liked = liked;
    }
}
