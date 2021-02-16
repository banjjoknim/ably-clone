package com.softsquared.template.src.market;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.config.statusEnum.Liked;
import com.softsquared.template.config.statusEnum.Satisfaction;
import com.softsquared.template.src.market.models.GetMarketMainInfoRes;
import com.softsquared.template.src.market.models.GetMarketsRes;
import com.softsquared.template.src.market.models.QGetMarketMainInfoRes;
import com.softsquared.template.src.market.models.QGetMarketsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.softsquared.template.DBmodel.QFavoriteMarket.favoriteMarket;
import static com.softsquared.template.DBmodel.QMarket.market;
import static com.softsquared.template.DBmodel.QMarketAndTag.marketAndTag;
import static com.softsquared.template.DBmodel.QMarketTag.marketTag;
import static com.softsquared.template.DBmodel.QProduct.product;
import static com.softsquared.template.DBmodel.QProductImage.productImage;
import static com.softsquared.template.DBmodel.QReview.review;
import static com.softsquared.template.config.Constant.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Repository
public class MarketQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public MarketQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<GetMarketsRes> getMarketsQuery(Market.MarketType marketType, Long categoryId,
                                               Long ageGroupId, Long marketTagId, PageRequest pageable) {
        return jpaQueryFactory
                .select(new QGetMarketsRes(
                        market.id,
                        market.name
                ))
                .from(market)
                .innerJoin(product).on(market.id.eq(product.marketId))
                .innerJoin(marketAndTag).on(market.id.eq(marketAndTag.marketId))
                .where(market.isPublic.eq(IsPublic.PUBLIC))
                .where(filterMarketTypeEq(marketType))
                .where(filterCategoryEq(categoryId))
                .where(filterAgeGroupEq(ageGroupId))
                .where(filterMarketTagEq(marketTagId))
                .groupBy(market.id, market.name)
                .offset(pageable.getPage() * pageable.getSize())
                .limit(pageable.getSize())
                .fetch();
    }

    public String getMarketTagsQuery(Long marketId) {
        return jpaQueryFactory
                .select(marketTag.name)
                .from(marketTag)
                .innerJoin(marketAndTag).on(marketAndTag.marketTagId.eq(marketTag.id))
                .where(marketAndTag.marketId.eq(marketId))
                .fetch()
                .stream()
                .collect(joining(WHITE_SPACE));
    }

    public List<String> getMarketThumbnailsQuery(Long marketId) {
        List<Product> products = getMarketProductsQuery(marketId);
        return products.stream()
                .map(selectedProduct ->
                        jpaQueryFactory
                                .select(productImage.image)
                                .from(productImage)
                                .where(productImage.productId.eq(selectedProduct.getId()))
                                .where(productImage.type.eq(ProductImage.ImageType.THUMBNAIL))
                                .fetchFirst()
                )
                .collect(toList());
    }

    private List<Product> getMarketProductsQuery(Long marketId) {
        return jpaQueryFactory
                .selectFrom(product)
                .where(product.marketId.eq(marketId))
                .orderBy(product.dateCreated.desc())
                .limit(MARKET_LIST_PRODUCT_THUMBNAIL_COUNT)
                .fetch();
    }

    private BooleanExpression filterMarketTypeEq(Market.MarketType marketType) {
        if (Market.MarketType.CELEB.equals(marketType)) {
            return market.marketType.eq(Market.MarketType.CELEB);
        }
        if (Market.MarketType.SHOPPINGMOLL.equals(marketType)) {
            return market.marketType.eq(Market.MarketType.SHOPPINGMOLL);
        }
        if (Market.MarketType.BRAND.equals(marketType)) {
            return market.marketType.eq(Market.MarketType.BRAND);
        }
        return null;
    }

    private BooleanExpression filterCategoryEq(Long categoryId) {
        if (categoryId != null) {
            return product.categoryId.eq(categoryId);
        }
        return null;
    }

    private BooleanExpression filterAgeGroupEq(Long ageGroupId) {
        if (ageGroupId != null) {
            return product.ageGroupId.eq(ageGroupId);
        }
        return null;
    }

    private BooleanExpression filterMarketTagEq(Long marketTagId) {
        if (marketTagId != null) {
            return marketAndTag.marketTagId.eq(marketTagId);
        }
        return null;
    }

    public GetMarketMainInfoRes getMarketMainInfoQuery(Long marketId) {
        return jpaQueryFactory
                .select(new QGetMarketMainInfoRes(
                        market.image,
                        market.marketType,
                        market.name,
                        getMarketProductCount(marketId),
                        getMarketReviewCount(marketId),
                        getMarketSatisfactionIsGoodReviewCount(marketId),
                        getMarketFavoriteCount(marketId)
                ))
                .from(market)
                .where(market.id.eq(marketId))
                .fetchFirst();
    }

    private JPQLQuery<Long> getMarketFavoriteCount(Long marketId) {
        return JPAExpressions
                .select(favoriteMarket.count())
                .from(favoriteMarket)
                .where(favoriteMarket.favoriteMarketId.marketId.eq(marketId).and(favoriteMarket.liked.eq(Liked.YES)));
    }

    private NumberExpression<Integer> getMarketSatisfactionIsGoodReviewCount(Long marketId) {
        return Expressions.asNumber(JPAExpressions
                .select(review.count())
                .from(review)
                .innerJoin(product).on(review.productId.eq(product.id))
                .where(product.marketId.eq(marketId).and(review.satisfaction.eq(Satisfaction.GOOD))))
                .divide(new CaseBuilder()
                        .when(getMarketTotalReviewCount(marketId).eq(ZERO))
                        .then(ONE)
                        .otherwise(getMarketTotalReviewCount(marketId)))
                .multiply(Expressions.asNumber(HUNDRED))
                .intValue();
    }

    private NumberExpression<Long> getMarketTotalReviewCount(Long marketId) {
        return Expressions.asNumber(JPAExpressions
                .select(review.count())
                .from(review)
                .innerJoin(product).on(review.productId.eq(product.id))
                .where(product.marketId.eq(marketId)));
    }

    private JPQLQuery<Long> getMarketProductCount(Long marketId) {
        return JPAExpressions.select(product.count())
                .from(product)
                .where(product.marketId.eq(marketId));
    }

    private JPQLQuery<Long> getMarketReviewCount(Long marketId) {
        return JPAExpressions.select(review.count())
                .from(review)
                .innerJoin(product).on(review.productId.eq(product.id))
                .where(product.marketId.eq(marketId));
    }
}
