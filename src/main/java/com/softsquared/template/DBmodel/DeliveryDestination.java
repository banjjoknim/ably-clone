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
    @Column(name = "desId", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long desId;

    @Column(name = "userId", nullable = false)
    private long userId;

    @Column(name = "detailAddress")
    private String detailAddress;

    @Column(name = "phoneNum",nullable = false)
    private String phoneNum;

    @Column(name = "userName",nullable = false)
    private String userName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "dateUpdated", nullable = false)
    private String dateUpdated;

    @Column(name = "dateCreated", nullable = false)
    private String dateCreated;

    @Column(name = "isMain", nullable = false)
    private int isMain;

    public DeliveryDestination(long deId, long userId, String detailAddress, String phoneNum,
                               String userName, String address, int status,
                               String dateUpdated, String dateCreated, int isMain){
        this.desId =deId;
        this.userId = userId;
        this.detailAddress = detailAddress;
        this.phoneNum = phoneNum;
        this.userName = userName;
        this.address = address;
        this.status =status;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.isMain = isMain;
    }
}
