package com.softsquared.template.src.favorite;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.favorite.models.GetFavoriteProductRes;
import com.softsquared.template.src.user.UserInfoRepository;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_USERS;
import static java.util.stream.Collectors.toList;

@Service
public class FavoriteProductProvider {

    private final FavoriteProductQueryRepository favoriteProductQueryRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;

    @Autowired
    public FavoriteProductProvider(FavoriteProductQueryRepository favoriteProductQueryRepository, UserInfoRepository userInfoRepository, JwtService jwtService) {
        this.favoriteProductQueryRepository = favoriteProductQueryRepository;
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
    }

    public List<GetFavoriteProductRes> retrieveFavoriteProducts() throws BaseException {
        long userId = jwtService.getUserId();
        if (!userInfoRepository.existsById(userId)) {
            throw new BaseException(NOT_FOUND_USERS);
        }

        List<GetFavoriteProductRes> favoriteProducts = favoriteProductQueryRepository.getFavoriteProductsQuery(userId);
        return favoriteProducts.stream()
                .map(favoriteProduct -> {
                    String thumbnail = favoriteProductQueryRepository.getFavoriteProductThumbnailQuery(favoriteProduct.getProductId());
                    favoriteProduct.setThumbnail(thumbnail);
                    return favoriteProduct;
                })
                .collect(toList());
    }

}
