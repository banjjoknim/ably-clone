package com.softsquared.template.src.product;

import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.product.models.GetProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse<List<GetProductsRes>> getProducts(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                          @RequestParam(value = "detailCategoryId", required = false) Long detailCategoryId) { // todo: RequestParam 추가해서 필터링 진행할 것.
        try {
            return new BaseResponse<>(SUCCESS, productProvider.retrieveProducts(categoryId, detailCategoryId));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(NOT_FOUND_CATEGORY.getMessage())) {
                return new BaseResponse<>(NOT_FOUND_CATEGORY);
            }
            if (e.getMessage().equals(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY.getMessage())) {
                return new BaseResponse<>(NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY);
            }
            return new BaseResponse<>(NOT_FOUND_DETAIL_CATEGORY);
        }
    }
}
