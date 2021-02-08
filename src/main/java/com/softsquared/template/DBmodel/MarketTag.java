package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@DynamicInsert
@Entity
@Table(name = "MarketTag")
public class MarketTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Builder
    public MarketTag(String name) {
        this.name = name;
    }
}
