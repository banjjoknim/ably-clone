package com.softsquared.template.src.product;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QProductOption.productOption;
import static com.softsquared.template.config.Constant.HUNDRED;

@Repository
public class ProductOptionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ProductOptionQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Integer getOptionPriceFromProduct(Long productId) {
        return jpaQueryFactory
                .select(ProductsQueryRepository.getDiscountedPrice())
                .from(product)
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    public Integer getOptionPriceFromProductOption(Long productOptionId) {
        return jpaQueryFactory
                .select(productOption.price
                        .divide(HUNDRED)
                        .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate)))
                .from(productOption)
                .innerJoin(product).on(productOption.productId.eq(product.id))
                .where(productOption.id.eq(productOptionId))
                .fetchFirst();
    }
}
