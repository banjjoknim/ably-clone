package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.ProductDetail;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.product.models.*;
import com.softsquared.template.src.review.ReviewQueryRepository;
import com.softsquared.template.src.review.models.ProductReviews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductProvider {

    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductImageQueryRepository productImageQueryRepository;
    private final ReviewQueryRepository reviewQueryRepository;

    @Autowired
    public ProductProvider(ProductRepository productRepository, ProductQueryRepository productQueryRepository, ProductImageQueryRepository productImageQueryRepository, ReviewQueryRepository reviewQueryRepository) {
        this.productRepository = productRepository;
        this.productQueryRepository = productQueryRepository;
        this.productImageQueryRepository = productImageQueryRepository;
        this.reviewQueryRepository = reviewQueryRepository;
    }

    public GetProductRes retrieveProduct(Long productId) throws BaseException {
        if (!productRepository.existsById(productId)) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_PRODUCT);
        }
        Long productCountInBasket = retrieveProductCountInBasket();
        ProductMainInfos productMainInfos = retrieveProductMainInfos(productId);
        ProductSubInfos productSubInfos = retrieveProductSubInfos(productId);
        ProductMarketInfos productMarketInfos = retrieveProductMarketInfos(productId);
        ProductDetailInfos productDetailInfos = retrieveProductDetailInfos(productId);
        ProductReviews productReviews = retrieveProductReviews(productId);
        Boolean productIsLiked = retrieveProductIsLiked(productId);
        Boolean productIsSale = retrieveProductIsSale(productId);

        return new GetProductRes(productCountInBasket, productMainInfos, productSubInfos,
                productMarketInfos, productDetailInfos, productReviews, productIsLiked, productIsSale);
    }

    private Long retrieveProductCountInBasket() {
        return productQueryRepository.getProductCountInBasketQuery();
    }

    private ProductMainInfos retrieveProductMainInfos(Long productId) {
        ProductMainInfo productMainInfo = productQueryRepository.getProductMainInfoQuery(productId);
        List<String> productThumbnails = productImageQueryRepository.getProductThumbnailsQuery(productId);

        return new ProductMainInfos(productMainInfo, productThumbnails);
    }

    private ProductSubInfos retrieveProductSubInfos(Long productId) {
        ProductSubInfo productSubInfo = productQueryRepository.getProductSubInfoQuery(productId);
        List<Integer> preparePeriodShares = productQueryRepository.getPreparePeriodSharesQuery(productId);

        return new ProductSubInfos(productSubInfo, preparePeriodShares);
    }

    private ProductMarketInfos retrieveProductMarketInfos(Long productId) {
        ProductMarketInfo productMarketInfo = productQueryRepository.getProductMarketInfoQuery(productId);
        String productMarketTags = productQueryRepository.getProductMarketTagsQuery(productId);

        return new ProductMarketInfos(productMarketInfo, productMarketTags);
    }

    private ProductDetailInfos retrieveProductDetailInfos(Long productId) {
        List<ProductDetailContent> productDetailTextAndImages = productQueryRepository.getProductDetailTextAndImagesQuery(productId);
        ProductModelInfo productModelInfo = productQueryRepository.getProductModelInfoQuery(productId);
        ProductDetail productDetail = productQueryRepository.getProductDetailQuery(productId);

        return new ProductDetailInfos(productDetailTextAndImages, productModelInfo, productDetail);
    }

    private ProductReviews retrieveProductReviews(Long productId) {
        return reviewQueryRepository.getProductReviews(productId);
    }

    private Boolean retrieveProductIsLiked(Long productId) {
        return productQueryRepository.getProductIsLikedQuery(productId);
    }

    private Boolean retrieveProductIsSale(Long productId) {
        return productQueryRepository.getProductIsSaleQuery(productId);
    }

}
