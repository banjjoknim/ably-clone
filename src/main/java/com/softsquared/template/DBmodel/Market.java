package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "Market")
public class Market extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "instagram")
    private String instagram;

    @NotNull
    @Column(name = "deliveryType")
    @Enumerated(value = EnumType.STRING)
    private DeliveryType deliveryType;

    @NotNull
    @Column(name = "marketType")
    @Enumerated(value = EnumType.STRING)
    private MarketType marketType;

    public enum DeliveryType {
        MARKET, ABLY,
    }

    public enum MarketType {
        CELEB, SHOPPINGMOLL, BRAND
    }

    @Builder
    public Market(@NotNull String name, @NotNull String image, @NotNull String instagram, @NotNull DeliveryType deliveryType, @NotNull MarketType marketType) {
        this.name = name;
        this.image = image;
        this.instagram = instagram;
        this.deliveryType = deliveryType;
        this.marketType = marketType;
    }
}
