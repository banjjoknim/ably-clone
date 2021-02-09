package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "MarketAndTag")
public class MarketAndTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "marketId")
    private Long marketId;

    @NotNull
    @Column(name = "marketTagId")
    private Long marketTagId;

    @Builder
    public MarketAndTag(Long marketId, Long marketTagId) {
        this.marketId = marketId;
        this.marketTagId = marketTagId;
    }
}
