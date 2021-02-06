package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
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
    @Column(name = "optionRank")
    @Enumerated(EnumType.STRING)
    private OptionRank optionRank;

    enum OptionRank {
        FIRST, SECOND
    }

    @Builder
    public ProductOption(Long productId, String optionName, OptionRank optionRank) {
        this.productId = productId;
        this.optionName = optionName;
        this.optionRank = optionRank;
    }
}
