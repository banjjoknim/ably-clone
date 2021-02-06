package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
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
@Table(name = "Review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "productId")
    private Long productId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "comment")
    private String comment;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "satisfaction")
    private Satisfaction satisfaction;

    @Column(name = "purchasedOption")
    private String purchasedOption;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "sizeComment")
    private SizeComment sizeComment;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "colorComment")
    private ColorComment colorComment;

    @NotNull
    @Column(name = "likesCount")
    private Long likesCount;

    public enum Satisfaction {
        GOOD, NORMAL, BAD
    }

    public enum SizeComment {
        BIG, FIT, SMALL
    }

    public enum ColorComment {
        BRIGHT, SCREEN_SAME, DARK
    }

    @Builder
    public Review(Long productId, Long userId, String comment, Satisfaction satisfaction, String purchasedOption, SizeComment sizeComment, ColorComment colorComment, @NotNull Long likesCount) {
        this.productId = productId;
        this.userId = userId;
        this.comment = comment;
        this.satisfaction = satisfaction;
        this.purchasedOption = purchasedOption;
        this.sizeComment = sizeComment;
        this.colorComment = colorComment;
        this.likesCount = likesCount;
    }
}
