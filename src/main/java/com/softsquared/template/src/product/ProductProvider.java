package com.softsquared.template.src.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.src.category.CategoryRepository;
import com.softsquared.template.src.category.DetailCategoryRepository;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductsInfo;
import com.softsquared.template.src.product.models.QProductsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.softsquared.template.DBmodel.QCeleb.celeb;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QPurchase.purchase;
import static com.softsquared.template.config.BaseResponseStatus.*;
import static java.util.stream.Collectors.toList;

@Service
public class ProductProvider {

    private final JPAQueryFactory jpaQueryFactory;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final DetailCategoryRepository detailCategoryRepository;

    @Autowired
    public ProductProvider(JPAQueryFactory jpaQueryFactory, ProductRepository productRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository, DetailCategoryRepository detailCategoryRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.detailCategoryRepository = detailCategoryRepository;
    }

    // todo : 상품 목록 조회시 유저 별 찜 반영 로직 추가해야 함.
    public List<GetProductsRes> retrieveProducts(Long categoryId, Long detailCategoryId) {

        if (categoryId != null && detailCategoryId != null) {
            validateCategory(categoryId, detailCategoryId);
        }

        List<ProductsInfo> productInfos = jpaQueryFactory
                .select(new QProductsInfo(
                        product.id,
                        product.discountRate,
                        product.price
                                .divide(100)
                                .multiply(Expressions.asNumber(100).subtract(product.discountRate)),
                        celeb.name,
                        product.name,
                        JPAExpressions
                                .select(ExpressionUtils.count(purchase)) // todo: 구매중 수 로직 추가해야 함.
                                .from(purchase)
                                .where(purchase.purProductCode.eq(product.id))
                ))
                .from(product)
                .innerJoin(celeb).on(product.celebId.eq(celeb.id))
                .where(filterCategory(categoryId, detailCategoryId)) // 카테고리 필터링 조건
                .fetch();

        return productInfos.stream()
                .map(productsInfo -> {
                    Long productId = productsInfo.getProductId();
                    boolean isNew = (new Date().getTime() - productRepository.findById(productId).get().getDateCreated().getTime()) <= 1000 * 60 * 60 * 24; // 등록된지 하루 이내이면 true
                    return new GetProductsRes(productsInfo, productImageRepository.findProductImageByProductId(productsInfo.getProductId()), true, isNew);
                })
                .collect(toList());
    }

    // 카테고리 필터링, null일경우 카테고리 필터링 조건에서 제외됨
    private Predicate filterCategory(Long categoryId, Long detailCategoryId) {

        BooleanBuilder builder = new BooleanBuilder();
        if (categoryId != null) {
            builder.and(product.categoryId.eq(categoryId));
        }
        if (detailCategoryId != null) {
            builder.and(product.detailCategoryId.eq(detailCategoryId));
        }
        return builder;
    }

    // 상위, 하위 카테고리 분류 일치여부 검증
    private void validateCategory(Long categoryId, Long detailCategoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_CATEGORY.getMessage());
        }
        if (detailCategoryRepository.findById(detailCategoryId).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY.getMessage());
        }
        if (detailCategoryRepository.findByIdAndCategoryId(detailCategoryId, categoryId).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY.getMessage());
        }
    }
}
