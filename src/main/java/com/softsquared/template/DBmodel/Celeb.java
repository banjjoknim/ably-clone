package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "Celeb")
public class Celeb extends BaseEntity {

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

    public enum DeliveryType{
        MARKET, ABLY
    }

    @Builder
    public Celeb(String name, String image, String instagram, DeliveryType deliveryType) {
        this.name = name;
        this.image = image;
        this.instagram = instagram;
        this.deliveryType = deliveryType;
    }
}
