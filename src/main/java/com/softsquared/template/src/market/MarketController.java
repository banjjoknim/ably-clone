package com.softsquared.template.src.market;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.market.models.PostMarketReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            if (request.getMarketName() == null) {
                throw new BaseException(MARKET_NAME_CAN_NOT_BE_EMPTY);
            }
            if (request.getDeliveryType() == null) {
                throw new BaseException(DELIVERY_TYPE_CAN_NOT_BE_EMPTY);
            }
            if (request.getMarketType() == null) {
                throw new BaseException(MARKET_TYPE_CAN_NOT_BE_EMPTY);
            }
            if (request.getImage() == null) {
                throw new BaseException(MARKET_IMAGE_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(SUCCESS, marketService.createMarket(request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
