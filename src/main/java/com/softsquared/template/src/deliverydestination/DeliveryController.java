package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.*;
import com.softsquared.template.src.deliverydestination.model.GetDeliveryRes;
import com.softsquared.template.src.deliverydestination.model.PostDeliveryReq;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/deliverydestinations")
public class DeliveryController {
    private final DeliveryProvider deliveryProvider;
    private final DeliveryService deliveryService;
    private final JwtService jwtService;
    private final FormatChecker formatChecker;


    @Autowired
    public DeliveryController(DeliveryProvider deliveryProvider, DeliveryService deliveryService, JwtService jwtService){
        this.deliveryProvider = deliveryProvider;
        this.deliveryService = deliveryService;
        this.jwtService = jwtService;
        formatChecker = new FormatChecker();

    }

    /**
     * 현재 사용자에게 등록된 배송지 정보 리스트 조회
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetDeliveryRes>> getUserDelivery(@RequestHeader("X-ACCESS-TOKEN") String token){
        long userId;
        try{
            userId = jwtService.getUserId();

        }catch(Exception e){
            return new BaseResponse<>(INVALID_TOKEN);
        }
        try{
            List<GetDeliveryRes> deliveryList = deliveryProvider.retrieveUserDelivery(userId);
            return new BaseResponse<>(SUCCESS, deliveryList);
        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/{deliverydestinationsid}")
    public BaseResponse<Boolean> deletedDelivery(@PathVariable long deliverydestinationsid){
        try{
            boolean result = deliveryService.deleteDeliveryDestination(deliverydestinationsid);
            return new BaseResponse<>(SUCCESS,result);
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(FAILED_TO_DELETE_DELVIERY_DESTINATION);
        }

    }

    /**
     * 배송지 추가
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postDelivery(@RequestHeader("X-ACCESS-TOKEN") String token,
                                             @RequestBody PostDeliveryReq param){
        if(token == null || token.length()==0)
            return new BaseResponse<>(EMPTY_JWT);

        if(!formatChecker.isFull(param.getPhoneNum())){
            return new BaseResponse<>(EMPTY_PHONENUM);
        }
        if(!formatChecker.isFull(param.getName())){
            return new BaseResponse<>(EMPTY_DELIVERY_NAME);
        }
        if(!formatChecker.isFull(param.getAddress())){
            return new BaseResponse<>(EMPTY_ADDRESS);
        }
        if(!formatChecker.isFull(param.getDetailAddress())){
            return new BaseResponse<>(EMPTY_DETAIL_ADDRESS);
        }
        if(!formatChecker.isPhoneNum(param.getPhoneNum())){
            return new BaseResponse<>(EMPTY_DETAIL_ADDRESS);
        }

        long userId;
        int isMain;

        try{
            userId = jwtService.getUserId();
        }catch (Exception e){
            return new BaseResponse<>(INVALID_TOKEN);
        }

        try{
            String result="";
            isMain = deliveryService.createDeliveryDestination(userId,param);
            if(isMain==1)
                result = "기본 배송지로 생성되었습니다";
            else
                result = "기본 배송지가 아닌 일반 배송지로 생성되었습니다";

            return new BaseResponse<>(SUCCESS,result);

        }catch (BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }



}
