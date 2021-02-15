package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.*;
import com.softsquared.template.src.deliverydestination.model.GetDeliveryRes;
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


    @Autowired
    public DeliveryController(DeliveryProvider deliveryProvider, DeliveryService deliveryService, JwtService jwtService){
        this.deliveryProvider = deliveryProvider;
        this.deliveryService = deliveryService;
        this.jwtService = jwtService;

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



}
