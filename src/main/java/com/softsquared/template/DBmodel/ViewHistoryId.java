package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Embeddable
public class ViewHistoryId implements Serializable {

    private Long userId;
    private Long productId;

    @Builder
    public ViewHistoryId(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
