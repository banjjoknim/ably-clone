package com.softsquared.template.src.favorite;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.favorite.models.GetFavoriteProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteProductProvider favoriteProductProvider;

    @Autowired
    public FavoriteController(FavoriteProductProvider favoriteProductProvider) {
        this.favoriteProductProvider = favoriteProductProvider;
    }

    @GetMapping("")
    public BaseResponse<List<GetFavoriteProductRes>> getFavoriteProducts() {
        try {
            return new BaseResponse<>(SUCCESS, favoriteProductProvider.retrieveFavoriteProducts());
        } catch (BaseException e) {
            if (e.getStatus().equals(EMPTY_JWT)) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            return new BaseResponse<>(NOT_FOUND_USERS);
        }
    }
}
