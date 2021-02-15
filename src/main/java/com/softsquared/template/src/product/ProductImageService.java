package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.src.product.models.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public void createProductImage(Long productId, PostProductReq request) {
        if (request.getProductImages() == null) {
            return;
        }
        request.getProductImages().stream()
                .map(image -> ProductImage.builder()
                        .productId(productId)
                        .image(image.getContent())
                        .type(image.getType())
                        .build())
                .forEach(productImage -> productImageRepository.save(productImage));
    }
}
