package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.*;
import com.softsquared.template.src.deliverydestination.model.GetDeliveryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/deliverydestinations")
public class DeliveryController {
    private final DeliveryProvider deliveryProvider;
    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryProvider deliveryProvider, DeliveryService deliveryService){
        this.deliveryProvider = deliveryProvider;
        this.deliveryService = deliveryService;

    }

    /**
     * 현재 사용자에게 등록된 배송지 정보 리스트 조회
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetDeliveryRes>> getUserDelivery(){
        try{
            List<GetDeliveryRes> deliveryList = deliveryProvider.retrieveUserDelivery(1);
            return new BaseResponse<>(SUCCESS, deliveryList);
        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 구매하기에서 하나의 배송지 선택
     * --> 구매하기의 모든 리소스들을 한번의 response로 보내는 것 vs 리소스별로 따로 보내는 것
     * --> 클라 여쭤보기
     *
     */
}
