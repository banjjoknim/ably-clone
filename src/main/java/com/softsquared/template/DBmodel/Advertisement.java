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
@Table(name = "Advertisement") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Advertisement {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "adCode", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adCode;


    @Column(name = "adName", nullable = false, updatable = false)
    private String adName;

    @Column(name = "adImg", nullable = true)
    private String adImg;

    @Column(name = "isDeleted", nullable = false)
    private int isDeleted;

    @Column(name = "dateUpdated", nullable = false)
    private String dateUpdated;

    @Column(name = "dateCreated", nullable = false)
    private String dateCreated;

    //화면에 표시될 것인지 아닌지
    //모든 광고를 표시하는 것이 아니니까
    @Column(name = "isBoard", nullable = false)
    private int isBoard;

    public Advertisement(long adCode, String adName, String adImg, int isDeleted, String dateUpdated,
                         String dateCreated, int isBoard){
        this.adCode = adCode;
        this.adName = adName;
        this.adImg = adImg;
        this.isDeleted = isDeleted;
        this.dateUpdated = dateUpdated;
        this.dateCreated = dateCreated;
        this.isBoard = isBoard;

    }

}
