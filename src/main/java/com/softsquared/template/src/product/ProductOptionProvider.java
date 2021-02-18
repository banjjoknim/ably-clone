package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.ProductOption;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.product.models.GetProductOptionEachRankRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_PRODUCT;
import static java.util.stream.Collectors.toList;

@Service
public class ProductOptionProvider {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionQueryRepository productOptionQueryRepository;

    @Autowired
    public ProductOptionProvider(ProductRepository productRepository, ProductOptionRepository productOptionRepository, ProductOptionQueryRepository productOptionQueryRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.productOptionQueryRepository = productOptionQueryRepository;
    }

    public GetProductOptionEachRankRes retrieveProductOptions(Long productId) throws BaseException {
        if (!productRepository.existsById(productId)) {
            throw new BaseException(NOT_FOUND_PRODUCT);
        }
        List<ProductOption> productOptions = productOptionRepository.findProductOptionsByProductId(productId);
        List<ProductOption> firstProductOptions = productOptions.stream()
                .filter(productOption -> productOption.getOptionRank().equals(ProductOption.OptionRank.FIRST))
                .collect(toList());
        List<ProductOption> secondProductOptions = productOptions.stream()
                .filter(productOption -> productOption.getOptionRank().equals(ProductOption.OptionRank.SECOND))
                .peek(productOption -> setProductOptionPrice(productId, productOption))
                .collect(toList());

        return new GetProductOptionEachRankRes(firstProductOptions, secondProductOptions);
    }

    private void setProductOptionPrice(Long productId, ProductOption productOption) {
        if (productOption.getPrice().equals(0)) {
            productOption.setPrice(productOptionQueryRepository.getOptionPriceFromProduct(productId));
            return;
        }
        productOption.setPrice(productOptionQueryRepository.getOptionPriceFromProductOption(productOption.getId()));
    }
}
