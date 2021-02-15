package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.ProductOption;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.product.models.GetProductOptionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_PRODUCT;
import static java.util.stream.Collectors.toList;

@Service
public class ProductOptionProvider {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Autowired
    public ProductOptionProvider(ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    public List<GetProductOptionRes> retrieveProductOptions(Long productId) throws BaseException {
        if (!productRepository.existsById(productId)) {
            throw new BaseException(NOT_FOUND_PRODUCT);
        }
        List<ProductOption> productOptions = productOptionRepository.findProductOptionsByProductId(productId);

        return productOptions.stream()
                .map(productOption -> new GetProductOptionRes(productOption.getId(), productOption.getOptionName(), productOption.getOptionRank()))
                .collect(toList());
    }
}
