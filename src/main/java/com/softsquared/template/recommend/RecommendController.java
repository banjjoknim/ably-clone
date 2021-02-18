package com.softsquared.template.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommends")
public class RecommendController {

    private final RecommendProvider recommendService;

    @Autowired
    public RecommendController(RecommendProvider recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping("")
    public List<GetRecommendedProductsRes> getRecommendedProducts(@RequestParam Integer page) {
        Long productId = 3L;
        return recommendService.getRecommendedProducts(productId, page);
    }
}
