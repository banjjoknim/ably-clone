package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.DBmodel.ProductDetail;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.product.models.*;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductProvider {

    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductImageQueryRepository productImageQueryRepository;
    private final JwtService jwtService;

    @Autowired
    public ProductProvider(ProductRepository productRepository, ProductQueryRepository productQueryRepository, ProductImageQueryRepository productImageQueryRepository, JwtService jwtService) {
        this.productRepository = productRepository;
        this.productQueryRepository = productQueryRepository;
        this.productImageQueryRepository = productImageQueryRepository;
        this.jwtService = jwtService;
    }

    // 가장 최근에 등록된 상품 조회
    public Optional<Product> retrieveMostRecentlyPostedProduct() {
        return productRepository.findFirstByIsPublicOrderByDateCreatedDesc(IsPublic.PUBLIC);
    }

    public GetProductRes retrieveProduct(Long productId) throws BaseException {
        if (!productRepository.existsById(productId)) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_PRODUCT);
        }
        Long userId;
        try {
            userId = jwtService.getUserId();
        } catch (BaseException e) { // 토큰이 없을 경우 -> 비회원인 경우
            userId = 0L;
        }

        Long productCountInBasket = retrieveProductCountInBasket(userId);
        ProductMainInfos productMainInfos = retrieveProductMainInfos(productId);
        ProductSubInfos productSubInfos = retrieveProductSubInfos(productId);
        ProductMarketInfos productMarketInfos = retrieveProductMarketInfos(productId);
        ProductDetailInfos productDetailInfos = retrieveProductDetailInfos(productId);
        Boolean productIsLiked = retrieveProductIsLiked(userId, productId);
        Boolean productIsSale = retrieveProductIsSale(productId);

        return new GetProductRes(productCountInBasket, productMainInfos, productSubInfos,
                productMarketInfos, productDetailInfos, productIsLiked, productIsSale);
    }

    private Long retrieveProductCountInBasket(Long userId) {
        return productQueryRepository.getProductCountInBasketQuery(userId);
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

    private Boolean retrieveProductIsLiked(Long userId, Long productId) {
        return productQueryRepository.getProductIsLikedQuery(userId, productId);
    }

    private Boolean retrieveProductIsSale(Long productId) {
        return productQueryRepository.getProductIsSaleQuery(productId);
    }

}
