package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.market.models.PatchMarketReq;
import com.softsquared.template.src.market.models.PostMarketReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.DUPLICATED_MARKET_NAME;
import static com.softsquared.template.config.BaseResponseStatus.NO_AUTHORITY;

@Transactional
@Service
public class MarketService {

    private final MarketRepository marketRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public Long createMarket(PostMarketReq request) throws BaseException {
        validateMarketName(request.getMarketName());
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

    public Long updateMarket(Long marketId, PatchMarketReq request) throws BaseException {
        validateMarketAuthority(marketId);
        Optional<Market> market = marketRepository.findById(marketId);
        if (market.isPresent()) {
            if (!market.get().getName().equals(request.getMarketName())) {
                validateMarketName(request.getMarketName());
            }
        }

        market.ifPresent(selectedMarket -> {
            selectedMarket.setName(request.getMarketName());
            selectedMarket.setImage(request.getImage());
            if (request.getInstagram() != null) {
                selectedMarket.setInstagram(request.getInstagram());
            }
            selectedMarket.setDeliveryType(request.getDeliveryType());
            selectedMarket.setMarketType(request.getMarketType());
            marketRepository.save(selectedMarket);
        });

        return marketId;
    }

    private void validateMarketName(String marketName) throws BaseException {
        if (marketRepository.existsByName(marketName)) {
            throw new BaseException(DUPLICATED_MARKET_NAME);
        }
    }

    public Long deleteMarket(Long marketId) throws BaseException {
        validateMarketAuthority(marketId);
        marketRepository.findById(marketId)
                .ifPresent(selectedMarket -> {
                    selectedMarket.setIsPublic(IsPublic.PRIVATE);
                    marketRepository.save(selectedMarket);
                });
        return marketId;
    }

    private void validateMarketAuthority(Long marketId) throws BaseException {
        Long marketIdFromToken = 15L;
        if (marketIdFromToken != marketId) {
            throw new BaseException(NO_AUTHORITY);
        }
    }
}
