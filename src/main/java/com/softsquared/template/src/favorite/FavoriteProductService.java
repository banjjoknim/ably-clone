package com.softsquared.template.src.favorite;

import com.softsquared.template.DBmodel.FavoriteProduct;
import com.softsquared.template.DBmodel.FavoriteProductId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.user.UserInfoRepository;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;

    @Autowired
    public FavoriteProductService(FavoriteProductRepository favoriteProductRepository, UserInfoRepository userInfoRepository, JwtService jwtService) {
        this.favoriteProductRepository = favoriteProductRepository;
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
    }

    public FavoriteProductId updateProductFavorite(Long productId) throws BaseException {
        Long userId = jwtService.getUserId();
        FavoriteProductId favoriteProductId = new FavoriteProductId(userId, productId);
        Optional<FavoriteProduct> favoriteProduct = favoriteProductRepository.findByFavoriteProductId(favoriteProductId);
        Long userCode = favoriteProduct.get().getFavoriteProductId().getUserCode();
        Long productCode = favoriteProduct.get().getFavoriteProductId().getProductCode();

        if (!userInfoRepository.existsById(userId)) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USERS);
        }

        if (favoriteProduct.isPresent()) {
            if (favoriteProduct.get().getLiked().equals(FavoriteProduct.Liked.YES)) {
                favoriteProductRepository.updateFavoriteIsNo(userCode, productCode);
                return favoriteProductId;
            }
            favoriteProductRepository.updateFavoriteIsYes(userCode, productCode);
            return favoriteProductId;
        }
        createProductFavorite(favoriteProductId);

        return favoriteProductId;
    }

    private void createProductFavorite(FavoriteProductId favoriteProductId) {
        FavoriteProduct favoriteProduct = new FavoriteProduct(favoriteProductId, FavoriteProduct.Liked.YES);
        favoriteProductRepository.save(favoriteProduct);
    }
}
