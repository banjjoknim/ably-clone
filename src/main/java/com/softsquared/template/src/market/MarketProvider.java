package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.src.market.models.GetMarketsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MarketProvider {

    private final MarketQueryRepository marketQueryRepository;

    @Autowired
    public MarketProvider(MarketQueryRepository marketQueryRepository) {
        this.marketQueryRepository = marketQueryRepository;
    }

    public List<GetMarketsRes> retrieveMarkets(Market.MarketType marketType, Long categoryId, Long ageGroupId, Long marketTagId) {
        List<GetMarketsRes> markets = marketQueryRepository.getMarketsQuery(marketType, categoryId, ageGroupId, marketTagId);
        markets.stream()
                .forEach(market -> market.setMarketTags(marketQueryRepository
                        .getMarketTagsQuery(market.getMarketId())));
        markets.stream()
                .forEach(market -> market.setThumbnails(marketQueryRepository
                        .getMarketThumbnailsQuery(market.getMarketId())));

        return markets;
    }
}
