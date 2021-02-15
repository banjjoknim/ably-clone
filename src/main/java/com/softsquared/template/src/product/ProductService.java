package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.src.product.models.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(Long marketId, PostProductReq request) {
        String productCode = getProductCode();
        Product product = Product.builder()
                .name(request.getProductName())
                .code(productCode)
                .marketId(marketId)
                .categoryId(request.getCategoryId())
                .detailCategoryId(request.getDetailCategoryId())
                .ageGroupId(request.getAgeGroupId())
                .clothLengthId(request.getClothLengthId())
                .colorId(request.getColorId())
                .fabricId(request.getFabricId())
                .tall(request.getTall())
                .fitId(request.getFitId())
                .printId(request.getPrintId())
                .modelId(request.getModelId())
                .price(request.getPrice())
                .discountRate(request.getDiscountRate())
                .build();
        productRepository.save(product);

        return product.getId();
    }

    private String getProductCode() {
        while (true) {
            String productCode = generateProductCode();
            if (!productRepository.existsByCode(productCode)) {
                return productCode;
            }
        }
    }

    private String generateProductCode() {
        StringBuilder stringBuilder = new StringBuilder();
        int dashPositionNumber = (int) (1 + Math.random() * 5);
        for (int i = 0; i < 13; i++) {
            if (i == dashPositionNumber) {
                stringBuilder.append("-");
                continue;
            }
            int number = (int) (1 + Math.random() * 9);
            stringBuilder.append(number);
        }
        return stringBuilder.toString();
    }
}
