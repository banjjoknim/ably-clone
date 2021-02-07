package com.softsquared.template.src.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.src.category.CategoryRepository;
import com.softsquared.template.src.category.DetailCategoryRepository;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductsInfo;
import com.softsquared.template.src.product.models.QProductsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
    private static final Integer EMPTY_SIZE = 0;

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

    // 상품 조회
    @Transactional(readOnly = true)
    public List<GetProductsRes> retrieveProducts(ProductFilterReq request) {

        validateCategoryFilter(request); // 카테고리 필터시 유효성 검증

        List<ProductsInfo> productInfos = getProductInfosQuery()
                .where(filterProductCategory(request)) // 카테고리 필터
                .where(filterProductDetail(request)) // 카테고리 필터 진행시, 상세 필터
                .fetch();

        return productInfos.stream()
                .map(productsInfo -> {
                    Long productId = productsInfo.getProductId();
                    boolean isNew = (new Date().getTime() - productRepository.findById(productId).get().getDateCreated().getTime()) <= 1000 * 60 * 60 * 24; // 등록된지 하루 이내이면 true
                    return new GetProductsRes(productsInfo, productImageRepository.findProductImageByProductId(productsInfo.getProductId()), true, isNew);
                })
                .collect(toList()); // 필터 적용된 결과 리스트 조회
    }

    // todo : 상품 목록 조회시 유저 별 찜 반영 쿼리 로직 추가해야 함.
    // 상품 조회 쿼리
    private JPAQuery<ProductsInfo> getProductInfosQuery() {
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
                .innerJoin(celeb).on(product.celebId.eq(celeb.id));

    }

    // 카테고리 필터 유효성 검증
    private void validateCategoryFilter(ProductFilterReq request) {

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CATEGORY.getMessage()));
        }
        if (request.getCategoryId() != null && request.getDetailCategoryId() != null) {
            validateCategoryAndDetailCategory(request);
        }
    }

    // todo: 세부 필터 유효성 검증 추가해야 함.
    // 카테고리 필터 유효성 검증
    private void validateCategoryAndDetailCategory(ProductFilterReq request) {

        if (detailCategoryRepository.findById(request.getDetailCategoryId()).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY.getMessage());
        }
        if (detailCategoryRepository.findByIdAndCategoryId(request.getDetailCategoryId(), request.getCategoryId()).isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY.getMessage());
        }
    }

    // 카테고리 필터 적용
    public Predicate filterProductCategory(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();
        if (request.getCategoryId() != null) {
            builder.and(product.categoryId.eq(request.getCategoryId()));
        }
        if (request.getDetailCategoryId() != null) {
            builder.and(product.detailCategoryId.eq(request.getDetailCategoryId()));
        }
        return builder;
    }

    // todo : 스타일 별 필터링 추가해야함
    // 카테고리 내의 상품 상세 필터링, null일경우 필터링 조건에서 제외됨
    public Predicate filterProductDetail(ProductFilterReq request) {

        BooleanBuilder builder = new BooleanBuilder();
        if (request.getMinimumPrice() != null) {
            if (request.getMinimumPrice() < 0) {
                throw new IllegalArgumentException(FILTER_PRICE_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(product.price
                    .divide(HUNDRED)
                    .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate))
                    .goe(request.getMinimumPrice()));
        }
        if (request.getMaximumPrice() != null) {
            if (request.getMaximumPrice() < 0) {
                throw new IllegalArgumentException(FILTER_PRICE_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(product.price
                    .divide(HUNDRED)
                    .multiply(Expressions.asNumber(HUNDRED).subtract(product.discountRate))
                    .loe(request.getMaximumPrice()));
        }
        if (request.getColorIds().length != EMPTY_SIZE) {
            Arrays.stream(request.getColorIds())
                    .forEach(colorId -> builder.or(product.colorId.eq(colorId)));
        }
        if (request.getPrintIds().length != EMPTY_SIZE) {
            Arrays.stream(request.getPrintIds())
                    .forEach(printId -> builder.or(product.printId.eq(printId)));
        }
        if (request.getFabricIds().length != EMPTY_SIZE) {
            Arrays.stream(request.getFabricIds())
                    .forEach(fabricId -> builder.or(product.fabricId.eq(fabricId)));
        }
        if (request.getMinimumTall() != null) {
            if (request.getMinimumTall() < 0) {
                throw new IllegalArgumentException(FILTER_PRICE_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(product.tall.goe(request.getMinimumTall()));
        }
        if (request.getMaximumTall() != null) {
            if (request.getMaximumTall() < 0) {
                throw new IllegalArgumentException(FILTER_PRICE_MUST_BE_POSITIVE.getMessage());
            }
            builder.and(product.tall.loe(request.getMaximumTall()));
        }
        if (request.getAgeGroupIds().length != EMPTY_SIZE) {
            Arrays.stream(request.getAgeGroupIds())
                    .forEach(ageGroupId -> builder.or(product.ageGroupId.eq(ageGroupId)));
        }
        if (request.getClothLengthIds().length != EMPTY_SIZE) {
            Arrays.stream(request.getClothLengthIds())
                    .forEach(clothLengthId -> builder.or(product.clothLengthId.eq(clothLengthId)));
        }
        return builder;
    }
}
