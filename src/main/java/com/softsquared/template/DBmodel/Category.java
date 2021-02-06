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
@Table(name = "Category") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Category {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "dateCreated", nullable = false, updatable = false)
    private String dateCreated;

    @Column(name = "dateUpdated", nullable = false, updatable = false)
    private String dateUpdated;

}
