package com.softsquared.template.DBmodel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "DeliveryDestination") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class DeliveryDestination {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "desCode", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long desCode;

    @Column(name = "userCode", nullable = false)
    private long userCode;

    @Column(name = "detailAddress")
    private String detailAddress;

    @Column(name = "phoneNum",nullable = false)
    private String phoneNum;

    @Column(name = "userName",nullable = false)
    private String userName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "isDeleted", nullable = false)
    private int isDeleted;

    @Column(name = "dateUpdated", nullable = false)
    private String dateUpdated;

    @Column(name = "dateCreated", nullable = false)
    private String dateCreated;
}
