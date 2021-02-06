package com.softsquared.template.src.product;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductsInfo;
import com.softsquared.template.src.product.models.QProductsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softsquared.template.DBmodel.QCeleb.celeb;
import static com.softsquared.template.DBmodel.QProduct.product;
import static java.util.stream.Collectors.toList;

@Service
public class ProductProvider {

    private final JPAQueryFactory jpaQueryFactory;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductProvider(JPAQueryFactory jpaQueryFactory, ProductImageRepository productImageRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.productImageRepository = productImageRepository;
    }

    // todo : 상품 목록 조회시 유저 별 찜 반영 로직 추가해야 함.
    public List<GetProductsRes> retrieveProducts() {

        List<ProductsInfo> productInfos = jpaQueryFactory
                .select(new QProductsInfo(
                        product.id,
                        product.discountRate,
                        product.price
                                .divide(100)
                                .multiply(Expressions.asNumber(100).subtract(product.discountRate)),
                        celeb.name,
                        product.name,
                        product.id // todo: 구매중 수 로직 추가해야 함.
                ))
                .from(product)
                .innerJoin(celeb).on(product.celebId.eq(celeb.id))
                .fetch();

        return productInfos.stream()
                .map(productsInfo -> new GetProductsRes(productsInfo, productImageRepository.findProductImageByProductId(productsInfo.getProductId()), true))
                .collect(toList());

    }

}
