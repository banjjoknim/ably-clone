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
public class PurchaseProduct {
    @Id
    @Column(name = "purProductId", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purProductId;


    //여러가지 제품을 묶어주는 수단
    @Column(name = "purchaseId", nullable = false, updatable = false)
    private long purchaseId;

    //제품 코드
    @Column(name = "productId", nullable = false, updatable = false)
    private long productId;

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
    private int status;

    //배송 상태
    //R - 배송 준비  C-배송 완 T- 주문 접수   D - 취소/반품
    @Column(name = "purStatus", nullable = false)
    private char purStatus;


    public PurchaseProduct(long purchaseId, long productId,int price,
                           String options, int count, int status,char purStatus){
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.price = price;
        this.options = options;
        this.count = count;
        this.status = status;
        this.purStatus = purStatus;
    }




}
