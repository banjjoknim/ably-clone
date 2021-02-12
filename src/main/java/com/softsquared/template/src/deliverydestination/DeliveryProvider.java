package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.deliverydestination.model.GetDelivery;
import com.softsquared.template.src.deliverydestination.model.GetDeliveryRes;
import com.softsquared.template.src.deliverydestination.model.GetMainDelivery;
import com.softsquared.template.src.deliverydestination.model.GetMainDeliveryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.softsquared.template.config.BaseResponseStatus.FAILED_TO_GET_DELVIERY_DESTINATION;
import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_DELIVERY_DESTINATION;

@Service
public class DeliveryProvider {
    private final DeliverySelectRepository deliverySelectRepository;

    @Autowired
    public DeliveryProvider(DeliverySelectRepository deliverySelectRepository){
        this.deliverySelectRepository = deliverySelectRepository;
    }

    /**
     * 해당 회원의 배송지 목록 찾기
     */
    public List<GetDeliveryRes> retrieveUserDelivery(long userId) throws BaseException {
        List<GetDelivery> deliveryList;
        try{
            deliveryList = deliverySelectRepository.findDeliveryByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }

        return changedDelToRes(deliveryList);
    }

    /**
     * 주문하기 진행하는 동안은 기본 배송지 하나만을 출력
     * 주문하기 진행 화면의 하나의 배송지 조회하기
     */
    public GetMainDeliveryRes retrieveMainDelivery(long userId) throws BaseException{
        List<GetMainDelivery> mainList;
        GetMainDeliveryRes mainDeliveryRes;
        try{
            mainList = deliverySelectRepository.findMainDeliveryByUserId(userId);
            if(mainList.size()==0)
                throw new BaseException(NOT_FOUND_DELIVERY_DESTINATION);
            mainDeliveryRes = changeMainDelToRes(mainList.get(0));

        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }
        return mainDeliveryRes;
    }




    public List<GetDeliveryRes> changedDelToRes(List<GetDelivery> list){
        List<GetDeliveryRes> changedList;
        changedList = list.stream().map( GetUserDelivery -> {
            String userName = GetUserDelivery.getUserName();
            String mainAdress = GetUserDelivery.getMainAddress();
            String subAdress = GetUserDelivery.getSubAddress();
            String phoneNum = GetUserDelivery.getPhoneNum();
            boolean isMain = (GetUserDelivery.getIsMain() ==1)? true : false;

            return new GetDeliveryRes(userName,mainAdress,subAdress,phoneNum,isMain);

        }).collect(Collectors.toList());

        return changedList;
    }

    public GetMainDeliveryRes changeMainDelToRes(GetMainDelivery main){
        GetMainDeliveryRes result;

          String   nameNum = main.getUserName() + "  " + main.getPhoneNum();
          String   mainAddress = main.getMainAddress();
           String  subAddress = main.getSubAddress();



        result = new GetMainDeliveryRes(nameNum,mainAddress,subAddress);
        return result;
    }
}
