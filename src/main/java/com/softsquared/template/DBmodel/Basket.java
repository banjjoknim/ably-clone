package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "Basket")
public class Basket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basketId")
    private Long basketId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "productCode")
    private Long productCode;

    @Column(name = "options")
    private String options;

    @Column(name = "count")
    private Integer count;

    @Column(name = "isDeleted")
    private int isDeleted;

    @Builder
    public Basket(Long userId, Long productCode, String options, Integer count, int isDeleted) {
        this.userId = userId;
        this.productCode = productCode;
        this.options = options;
        this.count = count;
        this.isDeleted = isDeleted;
    }
}
