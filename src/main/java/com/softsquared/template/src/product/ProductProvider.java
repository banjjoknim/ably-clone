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
import com.softsquared.template.src.product.models.ProductFilterReq;
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

    private static final Integer HUNDRED = 100;
    private static final Integer MINIMUM_PRICE_DIFFERENCE = 1000;
    private static final Integer MINIMUM_TALL_DIFFERENCE = 5;

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
    public List<GetProductsRes> retrieveProducts(ProductFilterReq request) {

        validateCategorys(request);

        List<ProductsInfo> productInfos = jpaQueryFactory
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
                .where(filterProducts(request)) // 카테고리 필터링 조건
                .fetch();

        return productInfos.stream()
                .map(productsInfo -> {
                    Long productId = productsInfo.getProductId();
                    boolean isNew = (new Date().getTime() - productRepository.findById(productId).get().getDateCreated().getTime()) <= 1000 * 60 * 60 * 24; // 등록된지 하루 이내이면 true
                    return new GetProductsRes(productsInfo, productImageRepository.findProductImageByProductId(productsInfo.getProductId()), true, isNew);
                })
                .collect(toList());
    }

    private void validateCategorys(ProductFilterReq request) {
        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CATEGORY.getMessage()));
        }
        if (request.getCategoryId() != null && request.getDetailCategoryId() != null) {
            validateCategoryAndDetailCategory(request);
        }
    }

    // todo: 세부 필터 유효성 검증 추가해야 함.
    // 상품 카테고리 필터 유효성 검증
    private void validateCategoryAndDetailCategory(ProductFilterReq request) {

        if (detailCategoryRepository.findById(request.getDetailCategoryId()).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY.getMessage());
        }
        if (detailCategoryRepository.findByIdAndCategoryId(request.getDetailCategoryId(), request.getCategoryId()).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY.getMessage());
        }
    }

    // todo : 스타일 별 필터링 추가해야함
    // 상품 필터링, null일경우 카테고리 필터링 조건에서 제외됨
    public Predicate filterProducts(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();
        if (request.getCategoryId() != null) {
            builder.and(product.categoryId.eq(request.getCategoryId()));
        }
        if (request.getDetailCategoryId() != null) {
            builder.and(product.detailCategoryId.eq(request.getDetailCategoryId()));
        }
        if (request.getMinimumPrice() != null) {
            builder.and(product.price
                    .divide(HUNDRED)
                    .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate))
                    .goe(request.getMinimumPrice()));
        }
        if (request.getMaximumPrice() != null) {
            builder.and(product.price
                    .divide(HUNDRED)
                    .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate))
                    .loe(request.getMaximumPrice()));
        }
        if (!request.getColorIds().isEmpty()) {
            request.getColorIds().stream()
                    .forEach(colorId -> builder.and(product.colorId.eq(colorId)));
        }
        if (!request.getPrintIds().isEmpty()) {
            request.getPrintIds().stream()
                    .forEach(printId -> builder.and(product.printId.eq(printId)));
        }
        if (!request.getFabricIds().isEmpty()) {
            request.getFabricIds().stream()
                    .forEach(fabricId -> builder.and(product.fabricId.eq(fabricId)));
        }
        if (request.getMinimumTall() != null) {
            builder.and(product.tall.goe(request.getMinimumTall()));
        }
        if (request.getMaximumTall() != null) {
            builder.and(product.tall.loe(request.getMaximumTall()));
        }
        if (!request.getAgeGroupIds().isEmpty()) {
            request.getAgeGroupIds().stream()
                    .forEach(ageGroupId -> builder.and(product.ageGroupId.eq(ageGroupId)));
        }
        if (!request.getClothLengthIds().isEmpty()) {
            request.getClothLengthIds().stream()
                    .forEach(clothLengthId -> builder.and(product.clothLengthId.eq(clothLengthId)));
        }
        return builder;
    }
}
