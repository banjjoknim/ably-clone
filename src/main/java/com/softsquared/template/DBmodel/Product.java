package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
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
    @Column(name = "celebId")
    private Long celebId;

    @NotNull
    @Column(name = "price")
    private Integer price;

    @NotNull
    @Column(name = "discountRate")
    private Integer discountRate;

    @Builder
    public Product(String code, String name, Long celebId, Integer price, Integer discountRate) {
        this.code = code;
        this.name = name;
        this.celebId = celebId;
        this.price = price;
        this.discountRate = discountRate;
    }
}
