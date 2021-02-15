package com.softsquared.template.src.favorite;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.config.statusEnum.Liked;
import com.softsquared.template.src.favorite.models.GetFavoriteProductRes;
import com.softsquared.template.src.favorite.models.QGetFavoriteProductRes;
import com.softsquared.template.src.product.ProductsQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.QFavoriteProduct.favoriteProduct;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QProductImage.productImage;

@Repository
public class FavoriteProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public FavoriteProductQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<GetFavoriteProductRes> getFavoriteProductsQuery(Long userId) {
        return jpaQueryFactory
                .select(new QGetFavoriteProductRes(
                        product.id,
                        product.discountRate,
                        ProductsQueryRepository.getDiscountedPrice(),
                        product.name
                ))
                .from(product)
                .innerJoin(favoriteProduct)
                .on(product.id.eq(favoriteProduct.favoriteProductId.productCode))
                .where(favoriteProduct.favoriteProductId.userCode.eq(userId).and(favoriteProduct.liked.eq(Liked.YES)))
                .fetch();
    }

    public String getFavoriteProductThumbnailQuery(Long productId) {
        return jpaQueryFactory
                .select(productImage.image)
                .from(productImage)
                .where(productImage.productId.eq(productId).and(productImage.type.eq(ProductImage.ImageType.THUMBNAIL)))
                .fetchFirst();
    }
}
