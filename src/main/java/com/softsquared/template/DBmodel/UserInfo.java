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
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userId", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userCode;

    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "phoneNum", nullable = false)
    private String phoneNum;

    @Column(name = "birthday")
    private int birthday;

    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private String weight;

    @Column(name = "topSize")
    private String topSize;

    @Column(name = "bottomSize")
    private String bottomSize;

    @Column(name = "shoeSize")
    private String shoeSize;

    @Column(name = "refundBank")
    private String refundBank;

    @Column(name = "refundName")
    private String refundName;

    @Column(name = "refundAccount")
    private String refundAccount;

    @Column(name = "point")
    private int point;

    @Column(name = "coupon")
    private int coupon;

    @Column(name = "rank", nullable = false)
    private String rank;

    @Column(name = "isDeleted", nullable = false)
    private int isDeleted;


    @Column(name = "dateUpdated", nullable = false)
    private String dateUpdated;


    @Column(name = "dateCreated", nullable = false)
    private String dateCreated;


    public UserInfo(long usrCode, String email, String userName, String phoneNum, int birthday,
                    String height, String weight, String topSize, String bottomSize, String shoeSize,
                    String refundName, String refundAccount, int point, int coupon, String rank,
                    int isDeleted, String dateUpdated, String dateCreated){
        this.userCode = usrCode;
        this.email = email;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.shoeSize = shoeSize;
        this.refundName = refundName;
        this.refundAccount = refundAccount;
        this.point = point;
        this.coupon = coupon;
        this.rank = rank;
        this.isDeleted = isDeleted;
        this.dateUpdated = dateUpdated ;
        this.dateCreated = dateCreated;
    }
}
