package com.softsquared.template.recommend;

import com.softsquared.template.src.product.models.GetProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<GetProductsRes> getRecommendedProducts(@RequestParam Integer page) {
        Long productId = 3L;
        return recommendService.getRecommendedProducts(productId, page);
    }
}
