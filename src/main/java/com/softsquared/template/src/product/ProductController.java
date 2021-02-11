package com.softsquared.template.src.product;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.product.models.GetProductRes;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductFilterReq;
import com.softsquared.template.src.product.models.ProductOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductProvider productProvider;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductProvider productProvider, ProductService productService) {
        this.productProvider = productProvider;
        this.productService = productService;
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
            return new BaseResponse<>(SUCCESS, productProvider.retrieveProducts(filterRequest, orderType));
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
            return new BaseResponse<>(SUCCESS, productProvider.retrieveProduct(productId));
        }
    }
