package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.market.models.GetMarketMainInfoRes;
import com.softsquared.template.src.market.models.GetMarketsRes;
import com.softsquared.template.src.product.ProductsProvider;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductOrderType;
import com.softsquared.template.src.review.ReviewProvider;
import com.softsquared.template.src.review.models.GetMarketReviewRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_MARKET;

@Transactional(readOnly = true)
@Service
public class MarketProvider {

    private final ProductsProvider productsProvider;
    private final ReviewProvider reviewProvider;
    private final MarketQueryRepository marketQueryRepository;
    private final MarketRepository marketRepository;

    @Autowired
    public MarketProvider(ProductsProvider productsProvider, ReviewProvider reviewProvider, MarketQueryRepository marketQueryRepository, MarketRepository marketRepository) {
        this.productsProvider = productsProvider;
        this.reviewProvider = reviewProvider;
        this.marketQueryRepository = marketQueryRepository;
        this.marketRepository = marketRepository;
    }

    public List<GetMarketsRes> retrieveMarkets(Market.MarketType marketType, Long categoryId,
                                               Long ageGroupId, Long marketTagId, PageRequest pageable) {
        List<GetMarketsRes> markets = marketQueryRepository
                .getMarketsQuery(marketType, categoryId, ageGroupId, marketTagId, pageable);
        markets.stream()
                .forEach(market -> market.setMarketTags(marketQueryRepository
                        .getMarketTagsQuery(market.getMarketId())));
        markets.stream()
                .forEach(market -> market.setThumbnails(marketQueryRepository
                        .getMarketThumbnailsQuery(market.getMarketId())));

        return markets;
    }

    public GetMarketMainInfoRes retrieveMarketMainInfo(Long marketId) throws BaseException {
        Optional<Market> market = marketRepository.findById(marketId);
        if (!market.isPresent() || market.get().getIsPublic().equals(IsPublic.PRIVATE)) {
            throw new BaseException(NOT_FOUND_MARKET);
        }
        GetMarketMainInfoRes marketMainInfo = marketQueryRepository.getMarketMainInfoQuery(marketId);
        marketMainInfo.setMarketTags(marketQueryRepository.getMarketTagsQuery(marketId));

        return marketMainInfo;
    }

    public List<GetProductsRes> retrieveMarketProducts(Long marketId, ProductOrderType orderType, PageRequest pageable) throws BaseException {
        Optional<Market> market = marketRepository.findById(marketId);
        if (!market.isPresent()) {
            throw new BaseException(NOT_FOUND_MARKET);
        }
        return productsProvider.retrieveMarketProducts(marketId, orderType, pageable);
    }

    public GetMarketReviewRes retrieveMarketReview(Long marketId, Long categoryId) throws BaseException {
        Optional<Market> market = marketRepository.findById(marketId);
        if (!market.isPresent()) {
            throw new BaseException(NOT_FOUND_MARKET);
        }
        return reviewProvider.retrieveMarketReview(marketId, categoryId);
    }
}
