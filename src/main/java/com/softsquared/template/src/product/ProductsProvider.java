package com.softsquared.template.src.product;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.src.category.CategoryRepository;
import com.softsquared.template.src.category.DetailCategoryRepository;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductOrderType;
import com.softsquared.template.src.purchase.model.GetPurchaseProduct;
import com.softsquared.template.src.purchase.model.GetPurchaseProductReq;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;
import static java.util.stream.Collectors.toList;

@Service
public class ProductsProvider {

    private final ProductQueryRepository productQueryRepository;
    private final ProductsQueryRepository productsQueryRepository;
    private final ProductImageQueryRepository productImageQueryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DetailCategoryRepository detailCategoryRepository;
    private final JwtService jwtService;

    @Autowired
    public ProductsProvider(ProductQueryRepository productQueryRepository, ProductsQueryRepository productsQueryRepository, ProductImageQueryRepository productImageQueryRepository, ProductRepository productRepository, CategoryRepository categoryRepository, DetailCategoryRepository detailCategoryRepository, JwtService jwtService) {
        this.productQueryRepository = productQueryRepository;
        this.productsQueryRepository = productsQueryRepository;
        this.productImageQueryRepository = productImageQueryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.detailCategoryRepository = detailCategoryRepository;
        this.jwtService = jwtService;
    }

    // 상품 조회
    @Transactional(readOnly = true)
    public List<GetProductsRes> retrieveProducts(ProductFilterReq filterRequest, ProductOrderType orderType, PageRequest pageable) throws BaseException {

        validateFilters(filterRequest); // 필터 유효성 검증

        return productsQueryRepository.getProductsInfos(filterRequest, orderType, pageable).stream()
                .map(productsInfo -> {
                    Long userId;
                    try {
                        userId = jwtService.getUserId();
                    } catch (BaseException e) {
                        userId = 0L;
                    }
                    Long productId = productsInfo.getProductId();
                    boolean isLiked = productQueryRepository.getProductIsLikedQuery(userId, productId);
                    boolean isNew = (new Date().getTime() - productRepository.findById(productId).get().getDateCreated().getTime()) <= 1000 * 60 * 60 * 24; // 등록된지 하루 이내이면 true
                    return new GetProductsRes(productsInfo, productImageQueryRepository.getProductThumbnailsQuery(productId), isLiked, isNew);
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

    /**
     * 구매할 상품의 정보 추출
     */
    public GetPurchaseProduct retrieveProductWithProductId(long productId) throws BaseException{
        GetPurchaseProduct product;
        try{
            product = productQueryRepository.findProductByProductId(productId).get(0);
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PRODUCT);
        }
        return product;
    }
}
