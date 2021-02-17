package com.softsquared.template.src.market;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.Constant;
import com.softsquared.template.config.PageRequest;
import com.softsquared.template.src.market.models.GetMarketMainInfoRes;
import com.softsquared.template.src.market.models.GetMarketsRes;
import com.softsquared.template.src.market.models.PatchMarketReq;
import com.softsquared.template.src.market.models.PostMarketReq;
import com.softsquared.template.src.product.models.GetProductsRes;
import com.softsquared.template.src.product.models.ProductOrderType;
import com.softsquared.template.src.review.models.GetMarketReviewRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;
import static com.softsquared.template.config.Constant.DEFAULT_PAGING_SIZE;

@RestController
@RequestMapping("/markets")
public class MarketController {

    private final MarketProvider marketProvider;
    private final MarketService marketService;

    @Autowired
    public MarketController(MarketProvider marketProvider, MarketService marketService) {
        this.marketProvider = marketProvider;
        this.marketService = marketService;
    }

    @GetMapping("")
    public BaseResponse<List<GetMarketsRes>> getMarkets(@RequestParam(required = false) Market.MarketType marketType,
                                                        @RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) Long ageGroupId,
                                                        @RequestParam(required = false) Long marketTagId,
                                                        @RequestParam Integer page) {
        PageRequest pageable = new PageRequest(page, Constant.MARKET_LIST_PAGING_SIZE);

        return new BaseResponse<>(SUCCESS, marketProvider.retrieveMarkets(marketType, categoryId, ageGroupId, marketTagId, pageable));
    }

    @GetMapping("/{marketId}/main-info")
    public BaseResponse<GetMarketMainInfoRes> getMarketMainInfo(@PathVariable Long marketId) {
        try {
            return new BaseResponse<>(SUCCESS, marketProvider.retrieveMarketMainInfo(marketId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{marketId}/products")
    public BaseResponse<List<GetProductsRes>> getMarketProducts(@PathVariable Long marketId,
                                                                @RequestParam(required = false) ProductOrderType orderType,
                                                                @RequestParam Integer page) {
        PageRequest pageable = new PageRequest(page, DEFAULT_PAGING_SIZE);

        try {
            return new BaseResponse<>(SUCCESS, marketProvider.retrieveMarketProducts(marketId, orderType, pageable));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{marketId}/reviews")
    public BaseResponse<GetMarketReviewRes> getMarketReview(@PathVariable Long marketId, @RequestParam(required = false) Long categoryId) {
        try {
            return new BaseResponse<>(SUCCESS, marketProvider.retrieveMarketReview(marketId, categoryId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
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

    @DeleteMapping("/{marketId}")
    public BaseResponse<Long> deleteMarket(@PathVariable Long marketId) {
        try {
            return new BaseResponse<>(SUCCESS, marketService.deleteMarket(marketId));
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
