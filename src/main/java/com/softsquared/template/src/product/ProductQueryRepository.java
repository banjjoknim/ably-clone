package com.softsquared.template.src.product;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.FavoriteProduct;
import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.DBmodel.ProductDetail;
import com.softsquared.template.DBmodel.Review;
import com.softsquared.template.src.product.models.*;
import com.softsquared.template.src.review.ReviewQueryRepository;
import com.softsquared.template.src.review.models.ProductReviews;
import com.softsquared.template.src.review.models.ReviewSummary;
import com.softsquared.template.src.review.models.ReviewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.Product.IsOnSale.ON_SALE;
import static com.softsquared.template.DBmodel.ProductImage.ImageType.*;
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
import static com.softsquared.template.src.product.ProductsQueryRepository.getDiscountedPrice;
import static java.util.stream.Collectors.joining;

@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ProductRepository productRepository;
    private final ReviewQueryRepository reviewQueryRepository;

    @Autowired
    public ProductQueryRepository(JPAQueryFactory jpaQueryFactory, ProductRepository productRepository, ReviewQueryRepository reviewQueryRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.productRepository = productRepository;
        this.reviewQueryRepository = reviewQueryRepository;
    }

    public GetProductRes getProductInfos(Long productId) {

        Long productCountInBasket = getProductCountInBasket();
        ProductMainInfos productMainInfos = getProductMainInfos(productId);
        ProductSubInfos productSubInfos = getProductSubInfos(productId);
        ProductMarketInfos productMarketInfos = getProductMarketInfos(productId);
        ProductDetailInfos productDetailInfos = getProductDetailInfos(productId);
        ProductReviews productReviews = reviewQueryRepository.getProductReviews(productId);
        Boolean productIsLiked = getProductIsLiked(productId);
        Boolean productIsSale = getProductIsSale(productId);

        return new GetProductRes(productCountInBasket, productMainInfos, productSubInfos,
                productMarketInfos, productDetailInfos, productReviews, productIsLiked, productIsSale);
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
                .from(product)
                .where(product.id.eq(productId))
                .fetchFirst();
    }

    public List<String> getProductThumbnails(Long productId) {
        return jpaQueryFactory
                .select(productImage.image)
                .from(productImage)
                .where(productImage.productId.eq(productId).and(productImage.type.eq(THUMBNAIL)))
                .fetch();
    }

    private ProductSubInfos getProductSubInfos(Long productId) {
        ProductSubInfo productSubInfo = getProductSubInfo(productId);
        List<Integer> preparePeriodShares = getPreparePeriodShares(productId);

        return new ProductSubInfos(productSubInfo, preparePeriodShares);
    }

    private ProductSubInfo getProductSubInfo(Long productId) {
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

    private List<Integer> getPreparePeriodShares(Long productId) {
        return List.of(98, 2, 0, 0);
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
        List<ProductDetailContent> productDetailTextAndImages = getProductDetailTextAndImages(productId);
        ProductModelInfo productModelInfo = getProductModelInfo(productId);
        ProductDetail productDetail = getProductDetail(productId);

        return new ProductDetailInfos(productDetailTextAndImages, productModelInfo, productDetail);
    }

    private List<ProductDetailContent> getProductDetailTextAndImages(Long productId) {
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

    public Long getProductCountInBasket() {
        Long userId = 1L;

        return jpaQueryFactory
                .select(basket)
                .from(basket)
                .where(basket.userId.eq(userId))
                .fetchCount();
    }

    public Boolean getProductIsLiked(Long productId) {
        Long userId = 1L;

        FavoriteProduct.Liked liked = jpaQueryFactory
                .select(favoriteProduct.liked)
                .from(favoriteProduct)
                .where(favoriteProduct.favoriteProductId.userCode.eq(userId).and(favoriteProduct.favoriteProductId.productCode.eq(productId)))
                .fetchFirst();

        if (FavoriteProduct.Liked.YES.equals(liked)) {
            return true;
        }
        return false;
    }

    private Boolean getProductIsSale(Long productId) {
        Product.IsOnSale isSale = productRepository.findById(productId).get().getIsOnSale();

        if (isSale.equals(ON_SALE)) {
            return true;
        }
        return false;
    }

}
