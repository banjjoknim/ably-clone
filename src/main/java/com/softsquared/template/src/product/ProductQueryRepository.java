package com.softsquared.template.src.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductsInfo;
import com.softsquared.template.src.product.models.QProductsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.QCeleb.celeb;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QPurchase.purchase;
import static com.softsquared.template.config.BaseResponseStatus.FILTER_PRICE_MUST_BE_POSITIVE;
import static com.softsquared.template.config.BaseResponseStatus.FILTER_TALL_MUST_BE_POSITIVE;

@Repository
public class ProductQueryRepository {

    private static final Integer HUNDRED = 100;

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ProductQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // todo : 상품 목록 조회시 유저 별 찜 반영 쿼리 로직 추가해야 함.
    // 상품 조회 쿼리
    public List<ProductsInfo> getProductsInfos(ProductFilterReq request) {

        return jpaQueryFactory
                .select(new QProductsInfo(
                        product.id,
                        product.discountRate,
                        product.price
                                .divide(HUNDRED)
                                .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate)),
                        celeb.name,
                        product.name,
                        JPAExpressions
                                .select(ExpressionUtils.count(purchase))
                                .from(purchase)
                                .where(purchase.purProductCode.eq(product.id))
                ))
                .from(product)
                .innerJoin(celeb).on(product.celebId.eq(celeb.id))
                .where(filterProductCategory(request)) // 카테고리 필터
                .where(filterProductPrice(request)) // 가격 필터
                .where(filterProductColor(request)) // 색상 필터
                .where(filterProductTallAndAgeGroup(request)) // 키 - 연령 필터
                .fetch();

    }

    // 카테고리 필터 적용
    public Predicate filterProductCategory(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getCategoryId().ifPresent(categoryId -> builder.and(product.categoryId.eq(categoryId)));
        request.getDetailCategoryId().ifPresent(detailCategoryId -> builder.and(product.detailCategoryId.eq(detailCategoryId)));

        return builder;
    }

    // 가격 필터 적용
    public Predicate filterProductPrice(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getMinimumPrice().ifPresent(minimumPrice -> {
            if (minimumPrice < 0) {
                throw new IllegalArgumentException(FILTER_PRICE_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(applyGoeOnMinimumPrice(minimumPrice));
        });

        request.getMaximumPrice().ifPresent(maximumPrice -> {
            if (maximumPrice < 0) {
                throw new IllegalArgumentException(FILTER_PRICE_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(applyLoeOnMaximumPrice(maximumPrice));
        });

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
    public Predicate filterProductTallAndAgeGroup(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        request.getMinimumTall().ifPresent(minimumTall -> {
            if (minimumTall < 0) {
                throw new IllegalArgumentException(FILTER_TALL_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(product.tall.goe(minimumTall));
        });

        request.getMaximumTall().ifPresent(maximumTall -> {
            if (maximumTall < 0) {
                throw new IllegalArgumentException(FILTER_TALL_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(product.tall.loe(maximumTall));
        });

        request.getAgeGroupIds().ifPresent(ageGroupIds -> ageGroupIds.stream().forEach(ageGroupId -> builder.or(product.fabricId.eq(ageGroupId))));
        request.getClothLengthIds().ifPresent(clothLengthIds -> clothLengthIds.stream().forEach(clothLengthId -> builder.or(product.fabricId.eq(clothLengthId))));

        return builder;
    }

    private BooleanExpression applyLoeOnMaximumPrice(Integer maximumPrice) {
        return product.price
                .divide(HUNDRED)
                .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate))
                .loe(maximumPrice);
    }

    private BooleanExpression applyGoeOnMinimumPrice(Integer minimumPrice) {
        return product.price
                .divide(HUNDRED)
                .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate))
                .goe(minimumPrice);
    }

}
