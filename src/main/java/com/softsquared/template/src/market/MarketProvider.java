package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.market.models.GetMarketMainInfoRes;
import com.softsquared.template.src.market.models.GetMarketsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_MARKET;

@Transactional(readOnly = true)
@Service
public class MarketProvider {

    private final MarketRepository marketRepository;
    private final MarketQueryRepository marketQueryRepository;

    @Autowired
    public MarketProvider(MarketRepository marketRepository, MarketQueryRepository marketQueryRepository) {
        this.marketRepository = marketRepository;
        this.marketQueryRepository = marketQueryRepository;
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
}
