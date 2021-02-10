package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "FavoriteProduct")
public class FavoriteProduct extends BaseEntity {

    @EmbeddedId
    private FavoriteProductId favoriteProductId;

    @Column(name = "liked")
    @Enumerated(EnumType.STRING)
    private Liked liked;

    public enum Liked {
        YES, NO
    }

    @Builder
    public FavoriteProduct(FavoriteProductId favoriteProductId, Liked liked) {
        this.favoriteProductId = favoriteProductId;
        this.liked = liked;
    }
}
