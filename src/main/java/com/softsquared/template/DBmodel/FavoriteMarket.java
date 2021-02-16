package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.statusEnum.Liked;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "FavoriteMarket")
public class FavoriteMarket extends BaseEntity {

    @EmbeddedId
    private FavoriteMarketId favoriteMarketId;

    @NotNull
    @Column(name = "liked")
    @Enumerated(EnumType.STRING)
    private Liked liked;

    @Builder
    public FavoriteMarket(FavoriteMarketId favoriteMarketId, @NotNull Liked liked) {
        this.favoriteMarketId = favoriteMarketId;
        this.liked = liked;
    }
}
