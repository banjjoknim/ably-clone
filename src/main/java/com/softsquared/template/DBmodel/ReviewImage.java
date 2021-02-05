package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ReviewImage")
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reviewId")
    private Long reviewId;

    @Column(name = "image")
    private String image;

    @Builder
    public ReviewImage(Long reviewId, String image) {
        this.reviewId = reviewId;
        this.image = image;
    }
}
