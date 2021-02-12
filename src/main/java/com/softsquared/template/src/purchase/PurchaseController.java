package com.softsquared.template.src.purchase;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.config.FormatChecker;
import com.softsquared.template.src.deliverydestination.DeliveryProvider;
import com.softsquared.template.src.deliverydestination.model.GetMainDeliveryRes;
import com.softsquared.template.src.purchase.model.GetPurchaseRes;
import org.springframework.web.bind.annotation.*;
import static com.softsquared.template.config.BaseResponseStatus.*;


import java.util.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private PurchaseProvider purchaseProvider;
    private PurchaseService purchaseService;
    private DeliveryProvider deliveryProvider;

    //request의 형식이 올바른지 check
    private FormatChecker formatChecker;

    public PurchaseController(PurchaseProvider purchaseProvider, PurchaseService purchaseService,
                              DeliveryProvider deliveryProvider){
        this.purchaseProvider = purchaseProvider;
        this.purchaseService = purchaseService;
        this.deliveryProvider = deliveryProvider;

        formatChecker = new FormatChecker();
    }


    /**
     * 구매하기에서 하나의 배송지 선택
     * 토큰 필
     *
     */
    @ResponseBody
    @GetMapping("/deliverydestinations")
    public BaseResponse<GetMainDeliveryRes> getMainDelivery(){
        try{
            GetMainDeliveryRes getMainDeliveryRes = deliveryProvider.retrieveMainDelivery(1);
            return new BaseResponse<>(SUCCESS, getMainDeliveryRes);
        }catch (BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }


//
//    @ResponseBody
//    @GetMapping("/refunds")
//    public Base

}
