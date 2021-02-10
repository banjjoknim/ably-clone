package com.softsquared.template.DBmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Embeddable
public class FavoriteProductId implements Serializable {

    private Long userCode;

    private Long productCode;

    @Builder
    public FavoriteProductId(Long userCode, Long productCode) {
        this.userCode = userCode;
        this.productCode = productCode;
    }
}
