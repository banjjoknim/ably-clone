package com.softsquared.template.recommend;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.product.models.GetProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/recommended-products")
public class RecommendController {

    private final RecommendProvider recommendService;

    @Autowired
    public RecommendController(RecommendProvider recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping("/main-page")
    public BaseResponse<List<GetProductsRes>> getRecommendedProducts(@RequestParam(required = false) Long productId, @RequestParam Integer page) {
        try {
            return new BaseResponse<>(SUCCESS, recommendService.getRecommendedProducts(productId, page));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
