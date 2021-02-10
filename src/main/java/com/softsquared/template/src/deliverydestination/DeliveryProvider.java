package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.deliverydestination.model.GetDelivery;
import com.softsquared.template.src.deliverydestination.model.GetDeliveryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.softsquared.template.config.BaseResponseStatus.FAILED_TO_GET_DELVIERY_DESTINATION;

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
        List <GetDelivery> deliveryList;
        try{
            deliveryList = deliverySelectRepository.findDeliveryByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }

        return changedDelToRes(deliveryList);
    }

//    /**
//     * 주문하기 진행하는 동안은 기본 배송지 하나만을 출력
//     * 주문하기 진행 화면의 하나의 배송지 조회하기
//     */
//    public List<GetDelivery




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
}
