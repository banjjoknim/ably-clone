package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "Model")
public class Model extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "tall")
    private Integer tall;

    @NotNull
    @Column(name = "topSize")
    private Integer topSize;

    @NotNull
    @Column(name = "bottomSize")
    private Integer bottomSize;

    @NotNull
    @Column(name = "shoeSize")
    private Integer shoeSize;

    @Autowired
    public Model(String name, String image, Integer tall, Integer topSize, Integer bottomSize, Integer shoeSize) {
        this.name = name;
        this.image = image;
        this.tall = tall;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.shoeSize = shoeSize;
    }
}
