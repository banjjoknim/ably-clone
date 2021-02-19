package com.softsquared.template.DBmodel;

import com.softsquared.template.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ViewHistory")
public class ViewHistory extends BaseEntity {

    @EmbeddedId
    private ViewHistoryId id;

    @Builder
    public ViewHistory(ViewHistoryId id) {
        this.id = id;
    }
}
