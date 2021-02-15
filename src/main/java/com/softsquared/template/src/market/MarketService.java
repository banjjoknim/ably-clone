package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.market.models.PostMarketReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.softsquared.template.config.BaseResponseStatus.DUPLICATED_MARKET_NAME;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public Long createMarket(PostMarketReq request) throws BaseException {
        if (marketRepository.existsByName(request.getMarketName())) {
            throw new BaseException(DUPLICATED_MARKET_NAME);
        }

        Market market = Market.builder()
                .name(request.getMarketName())
                .image(request.getImage())
                .instagram(request.getInstagram())
                .deliveryType(request.getDeliveryType())
                .marketType(request.getMarketType())
                .build();
        marketRepository.save(market);

        return market.getId();
    }
}
