package com.softsquared.template.DBmodel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo {

    @Id
    @Column(name = "userId", nullable = false, updatable = false)
    private long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "phoneNum", nullable = false)
    private String phoneNum;

    @Column(name = "birthday")
    private int birthday;

    @Column(name = "height", length = 20)
    private String height;

    @Column(name = "weight",length = 20)
    private String weight;

    @Column(name = "topSize",length = 20)
    private String topSize;

    @Column(name = "bottomSize",length = 20)
    private String bottomSize;

    @Column(name = "shoeSize",length = 20)
    private String shoeSize;

    @Column(name = "refundBank",length = 20)
    private String refundBank;

    @Column(name = "refundName",length = 20)
    private String refundName;

    @Column(name = "refundAccount",length = 45)
    private String refundAccount;

    @Column(name = "point", nullable = false)
    private int point;

    @Column(name = "coupon", nullable = false)
    private int coupon;

    @Column(name = "userRank", nullable = false)
    private String userRank;

    @Column(name = "status", nullable = false)
    private int status;


    @Column(name = "dateUpdated", nullable = false)
    private String dateUpdated;


    @Column(name = "dateCreated", nullable = false)
    private String dateCreated;

    @Column(name = "gender",length = 20)
    private String gender;

    @Column(name = "age",length = 20)
    private String age;

// for 회원가입
    public UserInfo(long userId, String email, String userName, String phoneNum, int birthday,String height, String weight,
                    String topSize, String bottomSize, String shoeSize, String refundBank, String refundName,
                    String refundAccount,int point, int coupon,String userRank,int status,
                    String dateUpdated, String dateCreated,String gender, String age){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.birthday = birthday;
        this.gender= gender;
        this.age = age;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.status= status;
        this.userRank = userRank;
        this.point = point;
        this.coupon = coupon;
        this.height = height;
        this.weight = weight;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.refundAccount = refundAccount;
        this.refundName = refundName;
        this.refundBank = refundBank;
        this.shoeSize = shoeSize;

    }
    public UserInfo(long userId, String email, String userName, String phoneNum, int birthday,
                    int point, int coupon,String userRank,int status,
                    String dateUpdated, String dateCreated,String gender, String age){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.birthday = birthday;
        this.gender= gender;
        this.age = age;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.status= status;
        this.userRank = userRank;
        this.point = point;
        this.coupon = coupon;

    }
    public UserInfo(long userId,String userName, String phoneNum,String refundBank, String refundName,
                    String refundAccount,int point, int coupon,String userRank,int status,
                    String dateUpdated, String dateCreated){
        this.userId = userId;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.refundBank = refundBank;
        this.refundName = refundName;
        this.refundAccount = refundAccount;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.status= status;
        this.userRank = userRank;
        this.point = point;
        this.coupon = coupon;
    }
}
