package com.softsquared.template.src.product;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.category.CategoryRepository;
import com.softsquared.template.src.category.DetailCategoryRepository;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;
import static java.util.stream.Collectors.toList;

@Service
public class ProductProvider {

    private final ProductQueryRepository productQueryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final DetailCategoryRepository detailCategoryRepository;

    @Autowired
    public ProductProvider(ProductQueryRepository productQueryRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository, DetailCategoryRepository detailCategoryRepository) {
        this.productQueryRepository = productQueryRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.detailCategoryRepository = detailCategoryRepository;
    }

    // 상품 조회
    @Transactional(readOnly = true)
    public List<GetProductsRes> retrieveProducts(ProductFilterReq filterRequest, ProductOrderType orderType) throws BaseException {

        validateFilters(filterRequest); // 필터 유효성 검증

        return productQueryRepository.getProductsInfos(filterRequest, orderType).stream()
                .map(productsInfo -> {
                    Long productId = productsInfo.getProductId();
                    boolean isNew = (new Date().getTime() - productRepository.findById(productId).get().getDateCreated().getTime()) <= 1000 * 60 * 60 * 24; // 등록된지 하루 이내이면 true
                    return new GetProductsRes(productsInfo, productImageRepository.findProductImageByProductId(productsInfo.getProductId()), true, isNew);
                })
                .collect(toList()); // 필터 적용된 결과 리스트 조회
    }

    // 필터 유효성 검증
    private void validateFilters(ProductFilterReq request) throws BaseException {

        validateCategoryFilter(request);
        validatePriceFilter(request);
        validateTallFilter(request);

    }

    private void validateCategoryFilter(ProductFilterReq request) throws BaseException {

        if (request.getCategoryId().isPresent()) {
            if (!categoryRepository.existsById(request.getCategoryId().get())) {
                throw new BaseException(NOT_FOUND_CATEGORY);
            }
        }
        if (request.getCategoryId().isPresent() && request.getDetailCategoryId().isPresent()) {
            Long categoryId = request.getCategoryId().get();
            Long detailCategoryId = request.getDetailCategoryId().get();

            if (!detailCategoryRepository.existsById(detailCategoryId)) {
                throw new BaseException(NOT_FOUND_DETAIL_CATEGORY);
            }
            if (!detailCategoryRepository.existsByIdAndCategoryId(detailCategoryId, categoryId)) {
                throw new BaseException(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY);
            }
        }
    }

    private void validateTallFilter(ProductFilterReq request) throws BaseException {

        if (request.getMinimumTall().isPresent() && request.getMinimumTall().get() < 0) {
            throw new BaseException(FILTER_TALL_MUST_BE_POSITIVE);
        }
        if (request.getMaximumTall().isPresent() && request.getMaximumTall().get() < 0) {
            throw new BaseException(FILTER_TALL_MUST_BE_POSITIVE);
        }
    }

    private void validatePriceFilter(ProductFilterReq request) throws BaseException {

        if (request.getMinimumPrice().isPresent() && request.getMinimumPrice().get() < 0) {
            throw new BaseException(FILTER_PRICE_MUST_BE_POSITIVE);
        }
        if (request.getMaximumPrice().isPresent() && request.getMaximumPrice().get() < 0) {
            throw new BaseException(FILTER_PRICE_MUST_BE_POSITIVE);
        }
    }

}
