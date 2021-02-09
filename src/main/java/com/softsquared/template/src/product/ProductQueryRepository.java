package com.softsquared.template.src.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Review;
import com.softsquared.template.src.product.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.softsquared.template.DBmodel.QMarket.market;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QProductImage.productImage;
import static com.softsquared.template.DBmodel.QPurchase.purchase;
import static com.softsquared.template.DBmodel.QReview.review;
import static com.softsquared.template.src.product.models.ProductOrderType.*;

@Repository
public class ProductQueryRepository {

    private static final Integer HUNDRED = 100;

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ProductQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // 상품 조회
    public List<ProductsInfo> getProductsInfos(ProductFilterReq filterRequest, ProductOrderType orderType) {

        JPAQuery<ProductsInfo> infosFilterQuery = productsInfosFilterQuery(getProductsInfosQuery(), filterRequest);
        return productsInfosOrderByQuery(infosFilterQuery, orderType).fetch();
    }

    // todo : 상품 목록 조회시 유저 별 찜 반영 쿼리 로직 추가해야 함.
    // 상품 조회 쿼리
    private JPAQuery<ProductsInfo> getProductsInfosQuery() {

        return jpaQueryFactory
                .select(new QProductsInfo(
                        product.id,
                        product.discountRate,
                        product.price
                                .divide(HUNDRED)
                                .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate)),
                        market.name,
                        product.name,
                        JPAExpressions
                                .select(count(purchase))
                                .from(purchase)
                                .where(purchase.purProductCode.eq(product.id)),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(review.id))
                                        .from(review)
                                        .where(product.id.eq(review.productId)), "reviewCount")
                ))
                .from(product)
                .innerJoin(market).on(product.marketId.eq(market.id));
    }

    private JPAQuery<ProductsInfo> productsInfosFilterQuery(JPAQuery<ProductsInfo> query, ProductFilterReq request) {

        return query
                .where(filterProductCategory(request)) // 카테고리 필터
                .where(filterProductPrice(request)) // 가격 필터
                .where(filterProductColor(request)) // 색상 필터
                .where(filterProductTallAndAgeGroup(request)); // 키 - 연령 필터
    }

    // todo : 인기순 정렬 로직 추가해야 함.
    private JPAQuery<ProductsInfo> productsInfosOrderByQuery(JPAQuery<ProductsInfo> query, ProductOrderType orderType) {

        if (LASTEST.equals(orderType)) {
            return query.orderBy(product.dateCreated.desc());
        }
        if (CHEAPEST.equals(orderType)) {
            return query.orderBy(getDiscountedPrice().asc());
        }
        if (REVIEWS.equals(orderType)) {
            return query.orderBy(new OrderSpecifier<>(Order.DESC, Alias.var("reviewCount")));
        }
        return query;
    }

    // 카테고리 필터 적용
    private Predicate filterProductCategory(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getCategoryId().ifPresent(categoryId -> builder.and(product.categoryId.eq(categoryId)));
        request.getDetailCategoryId().ifPresent(detailCategoryId -> builder.and(product.detailCategoryId.eq(detailCategoryId)));

        return builder;
    }

    // 가격 필터 적용
    private Predicate filterProductPrice(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getMinimumPrice().ifPresent(minimumPrice -> builder.and(minimumPriceGoe(minimumPrice)));
        request.getMaximumPrice().ifPresent(maximumPrice -> builder.and(maximumPriceLoe(maximumPrice)));

        return builder;
    }

    // 색상 필터 적용
    private Predicate filterProductColor(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getColorIds().ifPresent(colorIds -> colorIds.stream().forEach(colorId -> builder.or(product.colorId.eq(colorId))));
        request.getPrintIds().ifPresent(printIds -> printIds.stream().forEach(printId -> builder.or(product.printId.eq(printId))));
        request.getPrintIds().ifPresent(fabricIds -> fabricIds.stream().forEach(fabricId -> builder.or(product.fabricId.eq(fabricId))));

        return builder;
    }

    // todo : 스타일 별 필터링 추가해야함
    // 카테고리 내의 상품 상세 필터링, null일경우 필터링 조건에서 제외됨
    private Predicate filterProductTallAndAgeGroup(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getMinimumTall().ifPresent(minimumTall -> builder.and(product.tall.goe(minimumTall)));
        request.getMaximumTall().ifPresent(maximumTall -> builder.and(product.tall.loe(maximumTall)));
        request.getAgeGroupIds().ifPresent(ageGroupIds -> ageGroupIds.stream().forEach(ageGroupId -> builder.or(product.ageGroupId.eq(ageGroupId))));
        request.getClothLengthIds().ifPresent(clothLengthIds -> clothLengthIds.stream().forEach(clothLengthId -> builder.or(product.clothLengthId.eq(clothLengthId))));

        return builder;
    }

    private BooleanExpression maximumPriceLoe(Integer maximumPrice) {
        return getDiscountedPrice()
                .loe(maximumPrice);
    }

    private BooleanExpression minimumPriceGoe(Integer minimumPrice) {
        return getDiscountedPrice()
                .goe(minimumPrice);
    }

    private NumberExpression<Integer> getDiscountedPrice() {
        return product.price
                .divide(HUNDRED)
                .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate));
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

}
