package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.product.models.PostProductReq;
import com.softsquared.template.src.product.models.UpdateProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NO_AUTHORITY;

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

    public Long updateProduct(Long marketId, Long productId, UpdateProductReq request) throws BaseException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent() && !product.get().getMarketId().equals(marketId)) {
            throw new BaseException(NO_AUTHORITY);
        }

        product.ifPresent(selectedProduct -> {
            selectedProduct.setName(request.getProductName());
            selectedProduct.setCategoryId(request.getCategoryId());
            selectedProduct.setDetailCategoryId(request.getDetailCategoryId());
            selectedProduct.setAgeGroupId(request.getAgeGroupId());
            selectedProduct.setClothLengthId(request.getClothLengthId());
            selectedProduct.setColorId(request.getColorId());
            selectedProduct.setFabricId(request.getFabricId());
            selectedProduct.setTall(request.getTall());
            selectedProduct.setFitId(request.getFitId());
            selectedProduct.setPrintId(request.getPrintId());
            selectedProduct.setModelId(request.getModelId());
            selectedProduct.setPrice(request.getPrice());
            selectedProduct.setDiscountRate(request.getDiscountRate());
            selectedProduct.setIsOnSale(request.getIsOnSale());
            selectedProduct.setIsPublic(request.getIsPublic());
            productRepository.save(selectedProduct);
        });

        return productId;
    }
}
