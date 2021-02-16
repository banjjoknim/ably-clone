package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Embeddable
public class FavoriteMarketId implements Serializable {

    private Long userId;
    private Long marketId;

    @Builder
    public FavoriteMarketId(Long userId, Long marketId) {
        this.userId = userId;
        this.marketId = marketId;
    }
}
