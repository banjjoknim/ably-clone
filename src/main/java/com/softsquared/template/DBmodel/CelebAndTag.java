package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "CelebAndTag")
public class CelebAndTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "celebId")
    private Long celebId;

    @NotNull
    @Column(name = "celebTagId")
    private Long celebTagId;

    @Builder
    public CelebAndTag(Long celebId, Long celebTagId) {
        this.celebId = celebId;
        this.celebTagId = celebTagId;
    }
}
