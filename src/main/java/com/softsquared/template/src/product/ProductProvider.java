package com.softsquared.template.src.product;

import com.softsquared.template.src.category.CategoryRepository;
import com.softsquared.template.src.category.DetailCategoryRepository;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
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
    public List<GetProductsRes> retrieveProducts(ProductFilterReq request) {

        validateFilters(request); // 필터 유효성 검증

        return productQueryRepository.getProductsInfos(request).stream()
                .map(productsInfo -> {
                    Long productId = productsInfo.getProductId();
                    boolean isNew = (new Date().getTime() - productRepository.findById(productId).get().getDateCreated().getTime()) <= 1000 * 60 * 60 * 24; // 등록된지 하루 이내이면 true
                    return new GetProductsRes(productsInfo, productImageRepository.findProductImageByProductId(productsInfo.getProductId()), true, isNew);
                })
                .collect(toList()); // 필터 적용된 결과 리스트 조회
    }

    // 필터 유효성 검증
    private void validateFilters(ProductFilterReq request) {

        request.getCategoryId()
                .ifPresent(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CATEGORY.getMessage())));

        if (request.getCategoryId().isPresent() && request.getDetailCategoryId().isPresent()) {
            request.getDetailCategoryId()
                    .ifPresent(detailCategoryId -> detailCategoryRepository.findById(detailCategoryId)
                            .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY.getMessage())));

            detailCategoryRepository.findByIdAndCategoryId(request.getDetailCategoryId().get(), request.getCategoryId().get())
                    .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY.getMessage()));
        }
    }

}
