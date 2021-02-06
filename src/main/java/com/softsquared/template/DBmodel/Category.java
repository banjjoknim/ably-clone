package com.softsquared.template.DBmodel;


import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "Category")
public class Category extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Builder
    public Category(String name) {
        this.name = name;
    }
  
}
