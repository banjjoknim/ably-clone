package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@DynamicInsert
@Entity
@Table(name = "Purchase")
public class Purchase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purCode")
    private Long purCode;

    @Column(name = "purState")
    private String purState;

    @Column(name = "coupon")
    private Integer coupon;

    @Column(name = "point")
    private Integer point;

    @Column(name = "price")
    private Integer price;

    @Column(name = "desCode")
    private Integer desCode;

    @Column(name = "payment")
    private String payment;

    @Column(name = "purProductCode")
    private Long purProductCode;

    @Column(name = "isDeleted")
    private Integer isDeleted;

    @Builder
    public Purchase(String purState, Integer coupon, Integer point, Integer price, Integer desCode, String payment, Long purProductCode, Integer isDeleted) {
        this.purState = purState;
        this.coupon = coupon;
        this.point = point;
        this.price = price;
        this.desCode = desCode;
        this.payment = payment;
        this.purProductCode = purProductCode;
        this.isDeleted = isDeleted;
    }
}
