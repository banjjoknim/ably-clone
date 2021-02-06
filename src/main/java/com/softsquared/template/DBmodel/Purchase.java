package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "Purchase") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Purchase extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "purCode", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purCode;

    //배송 상태
    //R - 배송 준비  C-배송 완
    @Column(name = "purState", nullable = false)
    private char purState;

    @Column(name = "coupon")
    private int coupon;

    @Column(name = "point")
    private int point;

    @Column(name = "price", nullable = false)
    private int price;

    //배송지 코드 (여러 배송지 수단중 고른 코드)
    //DeliveryDestination table과 연
    @Column(name = "desCode", nullable = false)
    private long desCode;

    //결제 수단 코드
    //Payment table과 연관
    @Column(name = "paymentCode", nullable = false)
    private long paymentCode;

    //구매 상품 코드
    //PurchaseProduct table과 연관
    @Column(name = "purProductCode", nullable = false)
    private long purProductCode;


    @Column(name = "isDeleted", nullable = false)
    private int isDeleted;


    @Column(name = "userCode", nullable = false)
    private long userCode;

}
