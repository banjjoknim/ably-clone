package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import com.softsquared.template.config.statusEnum.ColorComment;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.config.statusEnum.SizeComment;
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

    @NotNull
    @Column(name = "productId")
    private Long productId;

    @NotNull
    @Column(name = "userId")
    private Long userId;

    @NotNull
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "satisfaction")
    private Satisfaction satisfaction;

    @NotNull
    @Column(name = "purchasedOption")
    private String purchasedOption;

    @Column(name = "form")
    private String form;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "sizeComment")
    private SizeComment sizeComment;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "colorComment")
    private ColorComment colorComment;

    @NotNull
    @Column(name = "likesCount")
    private Long likesCount;

    @Builder
    public Review(Long productId, Long userId, String comment, Satisfaction satisfaction, String purchasedOption, String form, SizeComment sizeComment, ColorComment colorComment, @NotNull Long likesCount) {
        this.productId = productId;
        this.userId = userId;
        this.comment = comment;
        this.satisfaction = satisfaction;
        this.purchasedOption = purchasedOption;
        this.form = form;
        this.sizeComment = sizeComment;
        this.colorComment = colorComment;
        this.likesCount = likesCount;
    }
}
