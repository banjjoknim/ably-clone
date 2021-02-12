package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.FavoriteProductId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.favorite.FavoriteProductService;
import com.softsquared.template.src.product.models.GetProductRes;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductOrderType;
import com.softsquared.template.src.review.ReviewProvider;
import com.softsquared.template.src.review.models.GetProductReviewsRes;
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
    private final ProductService productService;
    private final FavoriteProductService favoriteProductService;

    @Autowired
    public ProductController(ProductsProvider productsProvider, ProductProvider productProvider, ReviewProvider reviewProvider, ProductService productService, FavoriteProductService favoriteProductService) {
        this.productsProvider = productsProvider;
        this.productProvider = productProvider;
        this.reviewProvider = reviewProvider;
        this.productService = productService;
        this.favoriteProductService = favoriteProductService;
    }

    @GetMapping("")
    public BaseResponse<List<GetProductsRes>> getProducts(@RequestParam(required = false) Optional<Long> categoryId, @RequestParam(required = false) Optional<Long> detailCategoryId,
                                                          @RequestParam(required = false) Optional<Integer> minimumPrice, @RequestParam(required = false) Optional<Integer> maximumPrice,
                                                          @RequestParam(required = false) Optional<List<Long>> colorIds, @RequestParam(required = false) Optional<List<Long>> printIds,
                                                          @RequestParam(required = false) Optional<List<Long>> fabricIds, @RequestParam(required = false) Optional<Integer> minimumTall,
                                                          @RequestParam(required = false) Optional<Integer> maximumTall, @RequestParam(required = false) Optional<List<Long>> ageGroupIds,
                                                          @RequestParam(required = false) Optional<List<Long>> clothLengthIds, @RequestParam(required = false) ProductOrderType orderType) {

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

        try {
            return new BaseResponse<>(SUCCESS, productsProvider.retrieveProducts(filterRequest, orderType));
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
    public BaseResponse<GetProductReviewsRes> getProductReviews(@PathVariable(value = "productId") Long productId) {
        try {
            return new BaseResponse<>(SUCCESS, reviewProvider.retrieveProductReviews(productId));
        } catch (BaseException e) {
            return new BaseResponse<>(NOT_FOUND_PRODUCT);
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
            return new BaseResponse<>(NOT_FOUND_USERS);
        }
    }
}
