package com.softsquared.template.src.purchase;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.FormatChecker;
import com.softsquared.template.src.deliverydestination.DeliveryProvider;
import com.softsquared.template.src.deliverydestination.model.GetMainDeliveryRes;
import com.softsquared.template.src.purchase.model.GetPurchaseProductReq;
import com.softsquared.template.src.purchase.model.GetPurchaseProductRes;
import com.softsquared.template.src.purchase.model.GetPurchaseRefundRes;
import com.softsquared.template.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private PurchaseProvider purchaseProvider;
    private PurchaseService purchaseService;
    private DeliveryProvider deliveryProvider;
    private JwtService jwtService;

    //request의 형식이 올바른지 check
    private FormatChecker formatChecker;

    public PurchaseController(PurchaseProvider purchaseProvider, PurchaseService purchaseService,
                              DeliveryProvider deliveryProvider, JwtService jwtService){
        this.purchaseProvider = purchaseProvider;
        this.purchaseService = purchaseService;
        this.deliveryProvider = deliveryProvider;
        this.jwtService = jwtService;

        formatChecker = new FormatChecker();
    }

    /**
     * 구매하기 주문상품 조회 --> 아직 저장하는게 아니라 단순 조회임
     */
    @ResponseBody
    @GetMapping("/products")
    public BaseResponse<GetPurchaseProductRes> getPurchaseproduct (@RequestHeader("X-ACCESS-TOKEN") String token,
                                                                   @RequestParam long productId,
                                                                   @RequestParam List<String> options,
                                                                   @RequestParam List<Integer> num){
        long userId;
        try{
            userId = jwtService.getUserId();

        }catch(Exception e){
            e.printStackTrace();
            return new BaseResponse<>(INVALID_TOKEN);
        }
        if(options.size() != num.size()){
            return new BaseResponse<>(INVALID_SIZEOF_LIST);
        }
        try{
            GetPurchaseProductRes productRes = purchaseProvider.retrievePurchaseProduct(productId,options,num);
            return new BaseResponse<>(SUCCESS,productRes);
        }catch (BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }

    }



    /**
     * 구매하기에서 하나의 배송지 선택
     * 토큰 필
     *
     */
    @ResponseBody
    @GetMapping("/deliverydestinations")
    public BaseResponse<GetMainDeliveryRes> getMainDelivery(@RequestHeader("X-ACCESS-TOKEN") String token){
        long userId;
        try{
            userId = jwtService.getUserId();

        }catch(Exception e){
            e.printStackTrace();
            return new BaseResponse<>(INVALID_TOKEN);
        }
        try{
            GetMainDeliveryRes getMainDeliveryRes = purchaseProvider.retrievePurchaseMainAddress(userId);
            return new BaseResponse<>(SUCCESS, getMainDeliveryRes);
        }catch (BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }



    @ResponseBody
    @GetMapping("/refund-infos")
    public BaseResponse<GetPurchaseRefundRes> getPurchaseRefund(){
        try{
            GetPurchaseRefundRes getPurchaseRefundRes= purchaseProvider.retrievePurchaseRefundInfo(1);
            return new BaseResponse<>(SUCCESS, getPurchaseRefundRes);
        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

}
