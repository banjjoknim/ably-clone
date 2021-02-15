package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@DynamicInsert
@Entity
@Table(name = "ProductDetail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "productId")
    private Long productId;

    @NotNull
    @Column(name = "country")
    private String country;

    @NotNull
    @Column(name = "fabric")
    private String fabric;

    @NotNull
    @Column(name = "color")
    private String color;

    @NotNull
    @Column(name = "size")
    private String size;

    @NotNull
    @Column(name = "manufacturer")
    private String manufacturer;

    @NotNull
    @Column(name = "washType")
    private String washType;

    @NotNull
    @Column(name = "builtDate")
    private String builtDate;

    @NotNull
    @Column(name = "manager")
    private String manager;

    @NotNull
    @Column(name = "warranty")
    private String warranty;

    @Builder
    public ProductDetail(@NotNull Long productId, @NotNull String country, @NotNull String fabric, @NotNull String color, @NotNull String size, @NotNull String manufacturer, @NotNull String washType, @NotNull String builtDate, @NotNull String manager, @NotNull String warranty) {
        this.productId = productId;
        this.country = country;
        this.fabric = fabric;
        this.color = color;
        this.size = size;
        this.manufacturer = manufacturer;
        this.washType = washType;
        this.builtDate = builtDate;
        this.manager = manager;
        this.warranty = warranty;
    }
}
