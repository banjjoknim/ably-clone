package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.statusEnum.IsPublic;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@DynamicInsert
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
    private String topSize;

    @NotNull
    @Column(name = "bottomSize")
    private String bottomSize;

    @NotNull
    @Column(name = "shoeSize")
    private Integer shoeSize;

    @NotNull
    @Column(name = "marketId")
    private Long marketId;

    @NotNull
    @Column(name = "isPublic")
    @Enumerated(EnumType.STRING)
    private IsPublic isPublic;

    @Builder
    public Model(@NotNull String name, @NotNull String image, @NotNull Integer tall, @NotNull String topSize, @NotNull String bottomSize, @NotNull Integer shoeSize, @NotNull Long marketId, @NotNull IsPublic isPublic) {
        this.name = name;
        this.image = image;
        this.tall = tall;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.shoeSize = shoeSize;
        this.marketId = marketId;
        this.isPublic = isPublic;
    }
}
