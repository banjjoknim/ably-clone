package com.softsquared.template.src.history;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.DBmodel.ViewHistory;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.product.ProductProvider;
import com.softsquared.template.src.user.UserInfoRepository;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_USERS;
import static com.softsquared.template.config.BaseResponseStatus.NOT_HAVE_ANY_PRODUCTS;
import static com.softsquared.template.config.Constant.ZERO;

@Service
public class ViewHistoryProvider {

    private final UserInfoRepository userInfoRepository;
    private final ViewHistoryRepository viewHistoryRepository;
    private final ProductProvider productProvider;
    private final JwtService jwtService;

    @Autowired
    public ViewHistoryProvider(UserInfoRepository userInfoRepository, ViewHistoryRepository viewHistoryRepository, ProductProvider productProvider, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.viewHistoryRepository = viewHistoryRepository;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    public Long retrieveLastViewedProductId(String token) throws BaseException {
        Long userId = ZERO;
        if (token != null) {
            userId = jwtService.getUserId();
            if (userInfoRepository.findById(userId).isEmpty()) {
                throw new BaseException(NOT_FOUND_USERS);
            }
        }
        List<ViewHistory> lastViewHistory = viewHistoryRepository.findLastViewedProduct(userId);
        if (!lastViewHistory.isEmpty()) {
            return lastViewHistory.get(ZERO.intValue()).getId().getProductId();
        }
        Optional<Product> mostRecentlyPostedProduct = productProvider.retrieveMostRecentlyPostedProduct();
        if (mostRecentlyPostedProduct.isPresent()) {
            return mostRecentlyPostedProduct.get().getId();
        }
        throw new BaseException(NOT_HAVE_ANY_PRODUCTS);
    }
}
