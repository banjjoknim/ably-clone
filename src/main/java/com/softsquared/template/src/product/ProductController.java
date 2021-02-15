package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.FavoriteProductId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.Constant;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.src.favorite.FavoriteProductService;
import com.softsquared.template.src.product.models.*;
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
    private final ProductImageService productImageService;
    private final FavoriteProductService favoriteProductService;

    @Autowired
    public ProductController(ProductsProvider productsProvider, ProductProvider productProvider, ReviewProvider reviewProvider, ReviewService reviewService, ProductService productService, ProductImageService productImageService, FavoriteProductService favoriteProductService) {
        this.productsProvider = productsProvider;
        this.productProvider = productProvider;
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.productService = productService;
        this.productImageService = productImageService;
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
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("")
    public BaseResponse<Long> postProduct(@RequestBody PostProductReq request) {
        try {
            Long marketId = 3L;
            if (request.getProductName() == null) {
                throw new BaseException(PRODUCT_NAME_CAN_NOT_BE_EMPTY);
            }
            if (request.getCategoryId() == null) {
                throw new BaseException(CATEGORY_CAN_NOT_BE_EMPTY);
            }
            if (request.getDetailCategoryId() == null) {
                throw new BaseException(DETAIL_CATEGORY_CAN_NOT_BE_EMPTY);
            }
            if (request.getAgeGroupId() == null) {
                throw new BaseException(AGE_GROUP_CAN_NOT_BE_EMPTY);
            }
            if (request.getClothLengthId() == null) {
                throw new BaseException(CLOTH_LENGTH_CAN_NOT_BE_EMPTY);
            }
            if (request.getColorId() == null) {
                throw new BaseException(COLOR_CAN_NOT_BE_EMPTY);
            }
            if (request.getFabricId() == null) {
                throw new BaseException(FABRIC_CAN_NOT_BE_EMPTY);
            }
            if (request.getTall() == null) {
                throw new BaseException(TALL_CAN_NOT_BE_EMPTY);
            }
            if (request.getFitId() == null) {
                throw new BaseException(FIT_CAN_NOT_BE_EMPTY);
            }
            if (request.getPrintId() == null) {
                throw new BaseException(PRINT_CAN_NOT_BE_EMPTY);
            }
            if (request.getModelId() == null) {
                throw new BaseException(MODEL_CAN_NOT_BE_EMPTY);
            }
            if (request.getPrice() == null) {
                throw new BaseException(PRICE_CAN_NOT_BE_EMPTY);
            }
            Long productId = productService.createProduct(marketId, request);
            if (request.getProductImages() != null) {
                productImageService.createProductImage(productId, request);
            }
            return new BaseResponse<>(SUCCESS, productId);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{productId}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable(value = "productId") Long productId) {
        try {
            return new BaseResponse<>(SUCCESS, productProvider.retrieveProduct(productId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{productId}")
    public BaseResponse<Long> patchProduct(@PathVariable(value = "productId") Long productId, @RequestBody UpdateProductReq request) {
        try {
            Long marketId = 3L;
            if (request.getProductName() == null) {
                throw new BaseException(PRODUCT_NAME_CAN_NOT_BE_EMPTY);
            }
            if (request.getCategoryId() == null) {
                throw new BaseException(CATEGORY_CAN_NOT_BE_EMPTY);
            }
            if (request.getDetailCategoryId() == null) {
                throw new BaseException(DETAIL_CATEGORY_CAN_NOT_BE_EMPTY);
            }
            if (request.getAgeGroupId() == null) {
                throw new BaseException(AGE_GROUP_CAN_NOT_BE_EMPTY);
            }
            if (request.getClothLengthId() == null) {
                throw new BaseException(CLOTH_LENGTH_CAN_NOT_BE_EMPTY);
            }
            if (request.getColorId() == null) {
                throw new BaseException(COLOR_CAN_NOT_BE_EMPTY);
            }
            if (request.getFabricId() == null) {
                throw new BaseException(FABRIC_CAN_NOT_BE_EMPTY);
            }
            if (request.getTall() == null) {
                throw new BaseException(TALL_CAN_NOT_BE_EMPTY);
            }
            if (request.getFitId() == null) {
                throw new BaseException(FIT_CAN_NOT_BE_EMPTY);
            }
            if (request.getPrintId() == null) {
                throw new BaseException(PRINT_CAN_NOT_BE_EMPTY);
            }
            if (request.getModelId() == null) {
                throw new BaseException(MODEL_CAN_NOT_BE_EMPTY);
            }
            if (request.getPrice() == null) {
                throw new BaseException(PRICE_CAN_NOT_BE_EMPTY);
            }
            if (request.getDiscountRate() == null) {
                throw new BaseException(DISCOUNT_RATE_CAN_NOT_BE_EMPTY);
            }
            if (request.getIsOnSale() == null) {
                throw new BaseException(IS_ON_SALE_CAN_NOT_BE_EMPTY);
            }
            if (request.getIsPublic() == null) {
                throw new BaseException(IS_PUBLIC_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(SUCCESS, productService.updateProduct(marketId, productId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{productId}")
    public BaseResponse<Long> deleteProduct(@PathVariable(value = "productId") Long productId) {
        try {
            Long marketId = 3L;
            return new BaseResponse<>(SUCCESS, productService.deleteProduct(marketId, productId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{productId}/add-contents")
    public BaseResponse<Long> postProductImages(@PathVariable Long productId, @RequestBody PostProductImageReq request) {
        try {
            Long marketId = 3L;
            if (request.getProductImages() == null) {
                return new BaseResponse<>(SUCCESS, productId);
            }
            return new BaseResponse<>(SUCCESS, productImageService.addProductImage(marketId, productId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{productId}/remove-contents")
    public BaseResponse<Long> deleteProductImages(@PathVariable Long productId, @RequestBody DeleteProductImageReq request) {
        try {
            Long marketId = 3L;
            if (request.getProductImageIds() == null) {
                return new BaseResponse<>(SUCCESS, productId);
            }
            return new BaseResponse<>(SUCCESS, productImageService.removeProductImage(marketId, productId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{productId}/reviews")
    public BaseResponse<GetProductReviewsRes> getProductReviews(@PathVariable(value = "productId") Long productId, @RequestParam Integer page) {
        PageRequest pageable = new PageRequest(page, Constant.DEFAULT_PAGING_SIZE);
        try {
            return new BaseResponse<>(SUCCESS, reviewProvider.retrieveProductReviews(productId, pageable));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{productId}/reviews")
    public BaseResponse<Long> postProductReviews(@PathVariable(value = "productId") Long productId, @RequestBody PostProductReviewsReq request) {
        try {
            if (request.getSatisfaction() == null) {
                throw new BaseException(SATISFACTION_CAN_NOT_BE_EMPTY);
            }
            if (request.getPurchasedOptions() == null) {
                throw new BaseException(PURCHASED_OPTIONS_CAN_NOT_BE_EMPTY);
            }
            if (request.getSizeComment() == null) {
                throw new BaseException(SIZE_COMMENT_CAN_NOT_BE_EMPTY);
            }
            if (request.getColorComment() == null) {
                throw new BaseException(COLOR_COMMENT_CAN_NOT_BE_EMPTY);
            }
            if (request.getComment() == null) {
                throw new BaseException(COMMENT_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse(SUCCESS, reviewService.createProductReviews(productId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{productId}/favorite")
    public BaseResponse<FavoriteProductId> patchProductFavorite(@PathVariable(value = "productId") Long productId) {
        try {
            return new BaseResponse<>(SUCCESS, favoriteProductService.updateProductFavorite(productId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
