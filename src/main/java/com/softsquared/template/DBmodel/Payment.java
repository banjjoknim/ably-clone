package com.softsquared.template.DBmodel;

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
@Table(name = "Payment")
public class Payment extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "paymentCode", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentCode;

    @Column(name = "paymentName", nullable = false, updatable = false)
    private String purCode;


    @Column(name = "isDeleted", nullable = false, updatable = false)
    private int isDeleted;


}
