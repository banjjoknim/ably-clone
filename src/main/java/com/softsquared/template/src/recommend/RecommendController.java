package com.softsquared.template.src.recommend;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.history.ViewHistoryProvider;
import com.softsquared.template.src.product.models.GetProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/recommended-products")
public class RecommendController {

    private final RecommendProvider recommendService;
    private final ViewHistoryProvider viewHistoryProvider;

    @Autowired
    public RecommendController(RecommendProvider recommendService, ViewHistoryProvider viewHistoryProvider) {
        this.recommendService = recommendService;
        this.viewHistoryProvider = viewHistoryProvider;
    }

    @GetMapping("/main-page")
    public BaseResponse<List<GetProductsRes>> getRecommendedProducts(@RequestHeader(value = "X-ACCESS-TOKEN", required = false) String token, @RequestParam Integer page) {
        try {
            Long productId = viewHistoryProvider.retrieveLastViewedProductId(token);
            return new BaseResponse<>(SUCCESS, recommendService.getRecommendedProducts(productId, page));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
