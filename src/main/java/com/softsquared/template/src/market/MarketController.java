package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.market.models.PatchMarketReq;
import com.softsquared.template.src.market.models.PostMarketReq;
import org.springframework.web.bind.annotation.*;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/markets")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("")
    public BaseResponse<Long> postMarket(@RequestBody PostMarketReq request) {
        try {
            validateMarketRequest(request.getMarketName(), request.getDeliveryType(),
                    request.getMarketType(), request.getImage());
            return new BaseResponse<>(SUCCESS, marketService.createMarket(request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{marketId}")
    public BaseResponse<Long> patchMarket(@PathVariable Long marketId, @RequestBody PatchMarketReq request) {
        try {
            validateMarketRequest(request.getMarketName(), request.getDeliveryType(),
                    request.getMarketType(), request.getImage());
            return new BaseResponse<>(SUCCESS, marketService.updateMarket(marketId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    private void validateMarketRequest(String marketName, Market.DeliveryType deliveryType, Market.MarketType marketType, String image) throws BaseException {
        if (marketName == null) {
            throw new BaseException(MARKET_NAME_CAN_NOT_BE_EMPTY);
        }
        if (deliveryType == null) {
            throw new BaseException(DELIVERY_TYPE_CAN_NOT_BE_EMPTY);
        }
        if (marketType == null) {
            throw new BaseException(MARKET_TYPE_CAN_NOT_BE_EMPTY);
        }
        if (image == null) {
            throw new BaseException(MARKET_IMAGE_CAN_NOT_BE_EMPTY);
        }
    }
}
