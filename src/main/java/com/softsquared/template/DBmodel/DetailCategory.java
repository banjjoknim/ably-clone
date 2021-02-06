package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "DetailCategory")
public class DetailCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "categoryId")
    private Long categoryId;

    @NotNull
    @Column(name = "name")
    private String name;

    @Builder
    public DetailCategory(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
