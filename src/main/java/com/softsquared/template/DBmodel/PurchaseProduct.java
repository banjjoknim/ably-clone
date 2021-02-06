package com.softsquared.template.DBmodel;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.softsquared.template.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "PurchaseProduct")
public class PurchaseProduct extends BaseEntity {
    @Id
    @Column(name = "purProductIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purProductIdx;


    //여러가지 제품을 묶어주는 수단
    @Column(name = "purProductCode", nullable = false, updatable = false)
    private long purProductCode;

    //제품 코드
    @Column(name = "productCode", nullable = false, updatable = false)
    private long productCode;

    //가격
    @Column(name = "price", nullable = false, updatable = false)
    private int price;

    //제품 옵션
    @Column(name = "options", nullable = false, updatable = false)
    private String options;

    //수량
    @Column(name = "count", nullable = false, updatable = false)
    private int count;

    @Column(name = "isDeleted", nullable = false, updatable = false)
    private int isDeleted;





}
