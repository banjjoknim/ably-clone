package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "MacketAndTag")
public class MacketAndTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "macketId")
    private Long celebId;

    @NotNull
    @Column(name = "macketTagId")
    private Long celebTagId;

    @Builder
    public MacketAndTag(Long celebId, Long celebTagId) {
        this.celebId = celebId;
        this.celebTagId = celebTagId;
    }
}
