package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ProductImage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "productId")
    private Long productId;

    @NotNull
    @Column(name = "image")
    private String image;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private ImageType type;

    public enum ImageType{
        THUMBNAIL, DETAIL
    }

    @Builder
    public ProductImage(Long productId, String image, ImageType type) {
        this.productId = productId;
        this.image = image;
        this.type = type;
    }
}
