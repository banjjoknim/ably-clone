package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "ProductOption")
public class ProductOption {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "productId")
    private Long productId;

    @NotNull
    @Column(name = "optionName")
    private String optionName;

    @NotNull
    @Column(name = "price")
    private Integer price;

    @NotNull
    @Column(name = "optionRank")
    @Enumerated(EnumType.STRING)
    private OptionRank optionRank;

    public enum OptionRank {
        FIRST, SECOND, THIRD
    }

    @Builder
    public ProductOption(Long productId, String optionName, Integer price, OptionRank optionRank) {
        this.productId = productId;
        this.optionName = optionName;
        this.price = price;
        this.optionRank = optionRank;
    }
}
