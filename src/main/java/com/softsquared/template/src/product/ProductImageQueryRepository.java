package com.softsquared.template.src.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.ProductImage.ImageType.THUMBNAIL;
import static com.softsquared.template.DBmodel.QProductImage.productImage;

@Repository
public class ProductImageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ProductImageQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<String> getProductThumbnailsQuery(Long productId) {
        return jpaQueryFactory
                .select(productImage.image)
                .from(productImage)
                .where(productImage.productId.eq(productId).and(productImage.type.eq(THUMBNAIL)))
                .fetch();
    }
}
