package com.softsquared.template.src.market.models;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class GetMarketsRes {

    private Long marketId;
    private String marketName;
    private String marketTags;
    List<String> thumbnails;

    @QueryProjection
    public GetMarketsRes(Long marketId, String marketName) {
        this.marketId = marketId;
        this.marketName = marketName;
    }
}
