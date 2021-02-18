package com.softsquared.template.recommend;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.product.ProductRepository;
import com.softsquared.template.src.product.ProductsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class RecommendProvider {

    private final ProductsProvider productsProvider;
    private final ProductRepository productRepository;

    @Autowired
    public RecommendProvider(ProductsProvider productsProvider, ProductRepository productRepository) {
        this.productsProvider = productsProvider;
        this.productRepository = productRepository;
    }

    public List<GetRecommendedProductsRes> getRecommendedProducts(Long productId, Integer page) {
        Optional<Product> lastViewedProduct = productRepository.findById(productId);
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(product -> !product.getId().equals(lastViewedProduct.get().getId()))
                .filter(product -> product.getIsPublic().equals(IsPublic.PUBLIC))
                .map(product -> {
                    double[] compareProductScoreVector = new Vectorizer().getVector(lastViewedProduct.get(), product);
                    VectorCalculator vectorCalculator = new VectorCalculator(compareProductScoreVector);
                    return new GetRecommendedProductsRes(product, vectorCalculator.calculateCosineSimilarity());
                })
                .sorted(comparing(GetRecommendedProductsRes::getSimilarityScore).reversed())
                .collect(toList());
    }

}
