package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.product.models.DeleteProductImageReq;
import com.softsquared.template.src.product.models.PostProductImageReq;
import com.softsquared.template.src.product.models.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_PRODUCT;
import static com.softsquared.template.config.BaseResponseStatus.NO_AUTHORITY;

@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductImageService(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    public void createProductImage(Long productId, PostProductReq request) {
        request.getProductImages().stream()
                .map(image -> ProductImage.builder()
                        .productId(productId)
                        .image(image.getContent())
                        .type(image.getType())
                        .build())
                .forEach(productImage -> productImageRepository.save(productImage));
    }

    public Long addProductImage(Long marketId, Long productId, PostProductImageReq request) throws BaseException {
        validateIsExists(productId);
        validateAuthority(marketId, productId);
        request.getProductImages().stream()
                .map(image -> ProductImage.builder()
                        .productId(productId)
                        .image(image.getContent())
                        .type(image.getType())
                        .build())
                .forEach(productImage -> productImageRepository.save(productImage));

        return productId;
    }

    public Long removeProductImage(Long marketId, Long productId, DeleteProductImageReq request) throws BaseException {
        validateIsExists(productId);
        validateAuthority(marketId, productId);
        request.getProductImageIds().stream()
                .forEach(productImageId -> productImageRepository.findById(productImageId)
                        .ifPresent(productImage -> productImageRepository.deleteById(productImageId)));

        return productId;
    }

    private void validateIsExists(Long productId) throws BaseException {
        if (productRepository.findById(productId).isEmpty()) {
            throw new BaseException(NOT_FOUND_PRODUCT);
        }
    }

    private void validateAuthority(Long marketId, Long productId) throws BaseException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent() && !marketId.equals(product.get().getMarketId())) {
            throw new BaseException(NO_AUTHORITY);
        }
    }
}
