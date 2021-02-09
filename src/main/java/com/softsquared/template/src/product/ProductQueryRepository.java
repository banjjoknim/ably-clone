package com.softsquared.template.src.product;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Fabric;
import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.DBmodel.ProductDetail;
import com.softsquared.template.DBmodel.Review;
import com.softsquared.template.src.product.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.Product.IsSale.ON_SALE;
import static com.softsquared.template.DBmodel.ProductImage.ImageType.DETAIL;
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

    private ProductMainInfos getProductMainInfos(Long productId) {

        return new ProductMainInfos(getProductMainInfo(productId), getProductThumbnails(productId));
    }

    private ProductMainInfo getProductMainInfo(Long productId) {

        return jpaQueryFactory
                .select(new QProductMainInfo(
                        product.id,
                        product.name,
                        product.discountRate,
                        getDiscountedPrice(),
                        product.price,
                        product.code
                ))
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    private List<String> getProductThumbnails(Long productId) {

        return jpaQueryFactory
                .select(productImage.image)
                .from(productImage)
                .where(productImage.productId.eq(productId))
                .fetch();
    }

    private ProductSubInfo getProductSubInfo(Long productId) {
        return jpaQueryFactory
                .select(new QProductSubInfo(
                        JPAExpressions
                                .select(ExpressionUtils.count(review.id))
                                .from(review)
                                .where(review.productId.eq(productId)),
                        JPAExpressions
                                .select(ExpressionUtils.count(purchase.purCode))
                                .from(purchase)
                                .where(purchase.purProductCode.eq(productId)),
                        JPAExpressions
                                .select(
                                        review.count()
                                                .subtract(JPAExpressions
                                                        .select(ExpressionUtils.count(review))
                                                        .from(review)
                                                        .where(review.satisfaction.eq(Review.Satisfaction.BAD).and(review.productId.eq(productId))))
                                                .divide(review.count())
                                                .multiply(Expressions.asNumber(HUNDRED))
                                                .round()
                                                .intValue())
                                .from(review)
                                .where(review.productId.eq(productId)),
                        market.deliveryType
                ))
                .from(product)
                .innerJoin(market).on(product.marketId.eq(market.id))
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    private ProductMarketInfos getProductMarketInfos(Long productId) {
        return new ProductMarketInfos(getProductMarketInfo(productId), getProductMarketTags(productId));
    }

    private ProductMarketInfo getProductMarketInfo(Long productId) {

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

    private String getProductMarketTags(Long productId) {

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

    private ProductDetailInfos getProductDetailInfos(Long productId) {

        List<String> detailImages = getProductDetailImages(productId);
        ProductModelInfo productModelInfo = getProductModelInfo(productId);
        ProductDetail productDetail = getProductDetail(productId);

        return new ProductDetailInfos(detailImages, productModelInfo, productDetail);
    }

    private List<String> getProductDetailImages(Long productId) {

        return jpaQueryFactory
                .select(productImage.image)
                .from(productImage)
                .where(productImage.productId.eq(productId).and(productImage.type.eq(DETAIL)))
                .fetch();
    }

    private ProductModelInfo getProductModelInfo(Long productId) {

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

    private ProductDetail getProductDetail(Long productId) {

        return jpaQueryFactory
                .selectFrom(productDetail)
                .where(productDetail.productId.eq(productId))
                .fetchFirst();
    }

    // todo : 장바구니에 담긴 상품 갯수 계산 로직 추가해야 함.
    private Long getProductCountInBasket(Long productId) {

        return 0L;
    }

    // todo : 상품 찜하기 여부 로직 추가해야 함.
    private Boolean getProductIsLiked(Long productId) {

        return false;
    }

    private Boolean getProductIsSale(Long productId) {

        Product.IsSale isSale = productRepository.findById(productId).get().getIsSale();

        if (isSale.equals(ON_SALE)) {
            return true;
        }
        return false;
    }

}
