package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.statusEnum.Liked;
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

    @Builder
    public FavoriteProduct(FavoriteProductId favoriteProductId, Liked liked) {
        this.favoriteProductId = favoriteProductId;
        this.liked = liked;
    }
}
