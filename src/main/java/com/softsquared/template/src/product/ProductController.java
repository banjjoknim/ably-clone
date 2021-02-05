package com.softsquared.template.src.product;

import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.product.models.GetProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

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
    public BaseResponse<List<GetProductsRes>> getProducts() {
        return new BaseResponse<>(SUCCESS, productProvider.retrieveProducts());
    }

}
