package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.FavoriteProductId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.Constant;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.src.favorite.FavoriteProductService;
import com.softsquared.template.src.product.models.GetProductRes;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductOrderType;
import com.softsquared.template.src.review.ReviewProvider;
import com.softsquared.template.src.review.ReviewService;
import com.softsquared.template.src.review.models.GetProductReviewsRes;
import com.softsquared.template.src.review.models.PostProductReviewsReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductsProvider productsProvider;
    private final ProductProvider productProvider;
    private final ReviewProvider reviewProvider;
    private final ReviewService reviewService;
    private final ProductService productService;
    private final FavoriteProductService favoriteProductService;

    @Autowired
    public ProductController(ProductsProvider productsProvider, ProductProvider productProvider, ReviewProvider reviewProvider, ReviewService reviewService, ProductService productService, FavoriteProductService favoriteProductService) {
        this.productsProvider = productsProvider;
        this.productProvider = productProvider;
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.productService = productService;
        this.favoriteProductService = favoriteProductService;
    }

    @GetMapping("")
    public BaseResponse<List<GetProductsRes>> getProducts(@RequestParam(required = false) Optional<Long> categoryId, @RequestParam(required = false) Optional<Long> detailCategoryId,
                                                          @RequestParam(required = false) Optional<Integer> minimumPrice, @RequestParam(required = false) Optional<Integer> maximumPrice,
                                                          @RequestParam(required = false) Optional<List<Long>> colorIds, @RequestParam(required = false) Optional<List<Long>> printIds,
                                                          @RequestParam(required = false) Optional<List<Long>> fabricIds, @RequestParam(required = false) Optional<Integer> minimumTall,
                                                          @RequestParam(required = false) Optional<Integer> maximumTall, @RequestParam(required = false) Optional<List<Long>> ageGroupIds,
                                                          @RequestParam(required = false) Optional<List<Long>> clothLengthIds, @RequestParam(required = false) ProductOrderType orderType,
                                                          @RequestParam Integer page) {

        ProductFilterReq filterRequest = ProductFilterReq.builder()
                .categoryId(categoryId)
                .detailCategoryId(detailCategoryId)
                .minimumPrice(minimumPrice)
                .maximumPrice(maximumPrice)
                .colorIds(colorIds)
                .printIds(printIds)
                .fabricIds(fabricIds)
                .minimumTall(minimumTall)
                .maximumTall(maximumTall)
                .ageGroupIds(ageGroupIds)
                .clothLengthIds(clothLengthIds)
                .build();

        PageRequest pageable = new PageRequest(page, Constant.DEFAULT_PAGING_SIZE);

        try {
            return new BaseResponse<>(SUCCESS, productsProvider.retrieveProducts(filterRequest, orderType, pageable));
        } catch (BaseException e) {
            if (e.getStatus().equals(NOT_FOUND_CATEGORY)) {
                return new BaseResponse<>(NOT_FOUND_CATEGORY);
            }
            if (e.getStatus().equals(NOT_FOUND_DETAIL_CATEGORY)) {
                return new BaseResponse<>(NOT_FOUND_DETAIL_CATEGORY);
            }
            if (e.getStatus().equals(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY)) {
                return new BaseResponse<>(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY);
            }
            if (e.getStatus().equals(FILTER_PRICE_MUST_BE_POSITIVE)) {
                return new BaseResponse<>(FILTER_PRICE_MUST_BE_POSITIVE);
            }
            return new BaseResponse<>(FILTER_TALL_MUST_BE_POSITIVE);
        }
    }

    @GetMapping("/{productId}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable(value = "productId") Long productId) {
        try {
            return new BaseResponse<>(SUCCESS, productProvider.retrieveProduct(productId));
        } catch (BaseException e) {
            return new BaseResponse<>(NOT_FOUND_PRODUCT);
        }
    }

    @GetMapping("/{productId}/reviews")
    public BaseResponse<GetProductReviewsRes> getProductReviews(@PathVariable(value = "productId") Long productId, @RequestParam Integer page) {
        PageRequest pageable = new PageRequest(page, Constant.DEFAULT_PAGING_SIZE);
        try {
            return new BaseResponse<>(SUCCESS, reviewProvider.retrieveProductReviews(productId, pageable));
        } catch (BaseException e) {
            return new BaseResponse<>(NOT_FOUND_PRODUCT);
        }
    }

    @PostMapping("/{productId}/reviews")
    public BaseResponse<Long> postProductReviews(@PathVariable(value = "productId") Long productId, @RequestBody PostProductReviewsReq request) {
        try {
            return new BaseResponse(SUCCESS, reviewService.createProductReviews(productId, request));
        } catch (BaseException e) {
            if (e.getStatus().equals(EMPTY_JWT)) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            if (e.getStatus().equals(NOT_FOUND_USERS)) {
                return new BaseResponse<>(NOT_FOUND_USERS);
            }
            if (e.getStatus().equals(NOT_FOUND_PRODUCT)) {
                return new BaseResponse<>(NOT_FOUND_PRODUCT);
            }
            if (e.getStatus().equals(SATISFACTION_CAN_NOT_BE_EMPTY)) {
                return new BaseResponse<>(SATISFACTION_CAN_NOT_BE_EMPTY);
            }
            if (e.getStatus().equals(PURCHASED_OPTIONS_CAN_NOT_EMPTY)) {
                return new BaseResponse<>(PURCHASED_OPTIONS_CAN_NOT_EMPTY);
            }
            if (e.getStatus().equals(SIZE_COMMENT_CAN_NOT_BE_EMPTY)) {
                return new BaseResponse<>(SIZE_COMMENT_CAN_NOT_BE_EMPTY);
            }
            if (e.getStatus().equals(COLOR_COMMENT_CAN_NOT_BE_EMPTY)) {
                return new BaseResponse<>(COLOR_COMMENT_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(COMMENT_CAN_NOT_BE_EMPTY);
        }
    }

    @PatchMapping("/{productId}/favorite")
    public BaseResponse<FavoriteProductId> patchProductFavorite(@PathVariable(value = "productId") Long productId) {
        try {
            return new BaseResponse<>(SUCCESS, favoriteProductService.updateProductFavorite(productId));
        } catch (BaseException e) {
            if (e.getStatus().equals(EMPTY_JWT)) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            if (e.getStatus().equals(NOT_FOUND_PRODUCT)) {
                return new BaseResponse<>(NOT_FOUND_PRODUCT);
            }
            return new BaseResponse<>(NOT_FOUND_USERS);
        }
    }
}
