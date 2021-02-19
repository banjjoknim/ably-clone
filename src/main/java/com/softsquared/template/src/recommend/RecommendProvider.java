package com.softsquared.template.src.recommend;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.favorite.FavoriteProductProvider;
import com.softsquared.template.src.favorite.models.GetFavoriteProductRes;
import com.softsquared.template.src.product.ProductProvider;
import com.softsquared.template.src.product.ProductRepository;
import com.softsquared.template.src.product.ProductsProvider;
import com.softsquared.template.src.product.ProductsQueryRepository;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_PRODUCT;
import static com.softsquared.template.config.Constant.DEFAULT_PAGING_SIZE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class RecommendProvider {

    private final ProductsProvider productsProvider;
    private final ProductProvider productProvider;
    private final ProductsQueryRepository productsQueryRepository;
    private final ProductRepository productRepository;
    private final FavoriteProductProvider favoriteProductProvider;

    @Autowired
    public RecommendProvider(ProductsProvider productsProvider, ProductProvider productProvider, ProductsQueryRepository productsQueryRepository, ProductRepository productRepository, FavoriteProductProvider favoriteProductProvider) {
        this.productsProvider = productsProvider;
        this.productProvider = productProvider;
        this.productsQueryRepository = productsQueryRepository;
        this.productRepository = productRepository;
        this.favoriteProductProvider = favoriteProductProvider;
    }

    public List<GetProductsRes> getRecommendedProducts(Long productId, Integer page) throws BaseException {
        if (productId == null) {
            productId = productProvider.retrieveMostRecentlyPostedProduct().get().getId();
        }
        Optional<Product> lastViewedProduct = productRepository.findById(productId);
        if (lastViewedProduct.isEmpty()) {
            throw new BaseException(NOT_FOUND_PRODUCT);
        }
        List<Product> products = productRepository.findAll();
        PageRequest pageable = new PageRequest(page, DEFAULT_PAGING_SIZE);
        List<RecommendedProductInfo> recommendedProducts = getRecommendedProducts(productId, lastViewedProduct, products);

        return getRecommendedProducts(pageable, recommendedProducts);
    }

    // 후행 로직
    private List<GetProductsRes> getRecommendedProducts(PageRequest pageable, List<RecommendedProductInfo> recommendedProducts) {
        return recommendedProducts.stream()
                .map(product -> product.getProduct().getId())
                .filter(productId -> validateIsNotFavoriteProduct(productId))
                .map(id -> {
                    ProductsInfo recommendedProductsInfos = productsQueryRepository.getRecommendedProductsInfosQuery(id, pageable);
                    return productsProvider.getGetProductsRes(recommendedProductsInfos);
                })
                .collect(toList())
                .subList(pageable.getPage() * pageable.getSize(), pageable.getSize());
    }

    // 선행 로직
    private List<RecommendedProductInfo> getRecommendedProducts(Long productId, Optional<Product> lastViewedProduct, List<Product> products) {
        return products.stream()
                .filter(product -> !product.getId().equals(productId))
                .filter(product -> product.getIsPublic().equals(IsPublic.PUBLIC))
                .map(product -> {
                    double[] compareProductScoreVector = new Vectorizer().getScoreVector(lastViewedProduct.get(), product);
                    VectorCalculator vectorCalculator = new VectorCalculator(compareProductScoreVector);
                    return new RecommendedProductInfo(product, vectorCalculator.calculateCosineSimilarity());
                })
                .sorted(comparing(RecommendedProductInfo::getSimilarityScore).reversed())
                .collect(toList());
    }

    private boolean validateIsNotFavoriteProduct(Long productId) {
        List<GetFavoriteProductRes> favoriteProducts;
        try {
            favoriteProducts = favoriteProductProvider.retrieveFavoriteProducts();
        } catch (BaseException e) {
            return true;
        }

        return isNotFavoriteProduct(productId, favoriteProducts);
    }

    private boolean isNotFavoriteProduct(Long productId, List<GetFavoriteProductRes> favoriteProducts) {
        return favoriteProducts.stream()
                .map(favoriteProduct -> favoriteProduct.getProductId())
                .noneMatch(favoriteProductId -> favoriteProductId.equals(productId));
    }


}
