package com.softsquared.template.src.user;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.FormatChecker;
import com.softsquared.template.src.user.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/users")
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
   // private final JwtService jwtService;
    private final FormatChecker formatChecker;

    @Autowired
    public UserInfoController(UserInfoProvider userInfoProvider, UserInfoService userInfoService) {
        this.userInfoProvider = userInfoProvider;
        this.userInfoService = userInfoService;
       // this.jwtService = jwtService;
        formatChecker = new FormatChecker();
    }

    /**
     * 나의 구매 내역 조회
     * 이후 토큰을 통한 본인 확인 구현 필요
     */
    @ResponseBody
    @GetMapping("/{userId}/purchases")
    public BaseResponse<GetUsersPurchaseRes> getUserPurchases (@PathVariable long userId){
        try{
            GetUsersPurchaseRes list = userInfoProvider.retrieveUserPurchases(userId);
            return new BaseResponse<>(SUCCESS,list);
        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }



}