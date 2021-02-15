package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.statusEnum.IsOnSale;
import com.softsquared.template.config.statusEnum.IsPublic;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@DynamicInsert
@Entity
@Table(name = "Product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "marketId")
    private Long marketId;

    @NotNull
    @Column(name = "categoryId")
    private Long categoryId;

    @NotNull
    @Column(name = "detailCategoryId")
    private Long detailCategoryId;

    @NotNull
    @Column(name = "ageGroupId")
    private Long ageGroupId;

    @NotNull
    @Column(name = "clothLengthId")
    private Long clothLengthId;

    @NotNull
    @Column(name = "colorId")
    private Long colorId;

    @NotNull
    @Column(name = "fabricId")
    private Long fabricId;

    @NotNull
    @Column(name = "tall")
    private Integer tall;

    @NotNull
    @Column(name = "fitId")
    private Long fitId;

    @NotNull
    @Column(name = "printId")
    private Long printId;

    @NotNull
    @Column(name = "modelId")
    private Long modelId;

    @NotNull
    @Column(name = "price")
    private Integer price;

    @NotNull
    @Column(name = "discountRate")
    private Integer discountRate;

    @NotNull
    @Column(name = "isSale")
    @Enumerated(value = EnumType.STRING)
    private IsOnSale isOnSale;

    @NotNull
    @Column(name = "isPublic")
    @Enumerated(value = EnumType.STRING)
    private IsPublic isPublic;

    @Builder
    public Product(@NotNull String code, @NotNull String name, @NotNull Long marketId, @NotNull Long categoryId, @NotNull Long detailCategoryId, @NotNull Long ageGroupId, @NotNull Long clothLengthId, @NotNull Long colorId, @NotNull Long fabricId, @NotNull Integer tall, @NotNull Long fitId, @NotNull Long printId, @NotNull Long modelId, @NotNull Integer price, @NotNull Integer discountRate, @NotNull IsOnSale isOnSale, @NotNull IsPublic isPublic) {
        this.code = code;
        this.name = name;
        this.marketId = marketId;
        this.categoryId = categoryId;
        this.detailCategoryId = detailCategoryId;
        this.ageGroupId = ageGroupId;
        this.clothLengthId = clothLengthId;
        this.colorId = colorId;
        this.fabricId = fabricId;
        this.tall = tall;
        this.fitId = fitId;
        this.printId = printId;
        this.modelId = modelId;
        this.price = price;
        this.discountRate = discountRate;
        this.isOnSale = isOnSale;
        this.isPublic = isPublic;
    }
}
