package com.softsquared.template.src.product;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.ProductDetail;
import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.config.statusEnum.IsOnSale;
import com.softsquared.template.config.statusEnum.Liked;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.src.product.models.*;
import com.softsquared.template.src.purchase.model.GetPurchaseProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.ProductImage.ImageType.DETAIL;
import static com.softsquared.template.DBmodel.ProductImage.ImageType.TEXT;
import static com.softsquared.template.DBmodel.QBasket.basket;
import static com.softsquared.template.DBmodel.QFavoriteProduct.favoriteProduct;
import static com.softsquared.template.DBmodel.QMarket.market;
import static com.softsquared.template.DBmodel.QMarketAndTag.marketAndTag;
import static com.softsquared.template.DBmodel.QMarketTag.marketTag;
import static com.softsquared.template.DBmodel.QModel.model;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QProductDetail.productDetail;
import static com.softsquared.template.DBmodel.QProductImage.productImage;
import static com.softsquared.template.DBmodel.QPurchase.purchase;
import static com.softsquared.template.DBmodel.QReview.review;
import static com.softsquared.template.config.Constant.HUNDRED;
import static com.softsquared.template.config.statusEnum.IsOnSale.ON_SALE;
import static com.softsquared.template.src.product.ProductsQueryRepository.getDiscountedPrice;
import static java.util.stream.Collectors.joining;

@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ProductRepository productRepository;

    @Autowired
    public ProductQueryRepository(JPAQueryFactory jpaQueryFactory, ProductRepository productRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.productRepository = productRepository;
    }

    public ProductMainInfo getProductMainInfoQuery(Long productId) {
        return jpaQueryFactory
                .select(new QProductMainInfo(
                        product.id,
                        product.name,
                        product.discountRate,
                        getDiscountedPrice(),
                        product.price,
                        product.code
                ))
                .from(product)
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    public ProductSubInfo getProductSubInfoQuery(Long productId) {
        return jpaQueryFactory
                .select(new QProductSubInfo(
                        JPAExpressions
                                .select(ExpressionUtils.count(review.id))
                                .from(review)
                                .where(review.productId.eq(productId)),
                        JPAExpressions
                                .select(ExpressionUtils.count(purchase))
                                .from(purchase)
                                .where(purchase.purProductCode.eq(productId)),
                        JPAExpressions
                                .select(
                                        new CaseBuilder()
                                                .when(getSatisfactionRate(productId).isNotNull())
                                                .then(getSatisfactionRate(productId))
                                                .otherwise(Expressions.asNumber(0))
                                )
                                .from(review)
                                .where(review.productId.eq(productId)),
                        market.deliveryType
                ))
                .from(product)
                .innerJoin(market).on(product.marketId.eq(market.id))
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    private NumberExpression<Integer> getSatisfactionRate(Long productId) {
        return review.count()
                .subtract(JPAExpressions
                        .select(ExpressionUtils.count(review))
                        .from(review)
                        .where(review.satisfaction.eq(Satisfaction.BAD).and(review.productId.eq(productId))))
                .divide(review.count())
                .multiply(Expressions.asNumber(HUNDRED))
                .round()
                .intValue();
    }

    public List<Integer> getPreparePeriodSharesQuery(Long productId) {
        return List.of(57, 43, 0, 0);
    }

    public ProductMarketInfo getProductMarketInfoQuery(Long productId) {
        return jpaQueryFactory
                .select(new QProductMarketInfo(
                        product.marketId,
                        market.image,
                        market.name
                ))
                .from(product)
                .innerJoin(market).on(product.marketId.eq(market.id))
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    public String getProductMarketTagsQuery(Long productId) {
        return jpaQueryFactory
                .select(marketTag.name)
                .from(product)
                .innerJoin(market).on(product.marketId.eq(market.id))
                .innerJoin(marketAndTag).on(market.id.eq(marketAndTag.marketId))
                .innerJoin(marketTag).on(marketAndTag.marketTagId.eq(marketTag.id))
                .where(product.id.eq(productId))
                .fetch()
                .stream()
                .collect(joining(" "));
    }

    public List<ProductDetailContent> getProductDetailTextAndImagesQuery(Long productId) {
        return jpaQueryFactory
                .select(new QProductDetailContent(
                        productImage.image,
                        productImage.type
                ))
                .from(productImage)
                .where(productImage.productId.eq(productId))
                .where(productImage.type.eq(TEXT).or(productImage.type.eq(DETAIL)))
                .orderBy(productImage.id.asc())
                .fetch();
    }

    public ProductModelInfo getProductModelInfoQuery(Long productId) {
        return jpaQueryFactory
                .select(new QProductModelInfo(
                        model.image,
                        model.name,
                        model.tall,
                        model.topSize,
                        model.bottomSize,
                        model.shoeSize
                ))
                .from(product)
                .innerJoin(model).on(product.modelId.eq(model.id))
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    public ProductDetail getProductDetailQuery(Long productId) {
        return jpaQueryFactory
                .selectFrom(productDetail)
                .where(productDetail.productId.eq(productId))
                .fetchFirst();
    }

    public Long getProductCountInBasketQuery(Long userId) {

        return jpaQueryFactory
                .select(basket)
                .from(basket)
                .where(basket.userId.eq(userId))
                .fetchCount();
    }

    public Boolean getProductIsLikedQuery(Long userId, Long productId) {

        Liked liked = jpaQueryFactory
                .select(favoriteProduct.liked)
                .from(favoriteProduct)
                .where(favoriteProduct.favoriteProductId.userCode.eq(userId).and(favoriteProduct.favoriteProductId.productCode.eq(productId)))
                .fetchFirst();

        if (Liked.YES.equals(liked)) {
            return true;
        }
        return false;
    }

    public Boolean getProductIsSaleQuery(Long productId) {
        IsOnSale isSale = productRepository.findById(productId).get().getIsOnSale();

        if (isSale.equals(ON_SALE)) {
            return true;
        }
        return false;
    }

    /**
     * 구매하기에서 구매하고자 하는 상품에 대한 정보 출력하기 위해
     */
    public List<GetPurchaseProduct> findProductByProductId(long productId){
//        QProduct product = QProduct.product;
//        QProductImage image = QProductImage.productImage;
//        QMarket market = QMarket.market;
        return jpaQueryFactory.select((Projections.constructor(GetPurchaseProduct.class,
                productImage.image, market.name,product.name, product.price,
                product.discountRate)))
                .from(product)
                .innerJoin(productImage)
                .on(productImage.productId.eq(productId), productImage.type.eq(ProductImage.ImageType.THUMBNAIL))
                .innerJoin(market)
                .on(product.marketId.eq(market.id))
                .where(product.id.eq(productId))
                .fetch();

    }
}
