package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.deliverydestination.model.*;
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

    /**
     * 삭제할 배송지 검색하기 --> Service로 넘어감
     *
     */
    public DeleteDelivery retrieveDeleteDelivery(long desId)throws BaseException{
        DeleteDelivery deliveryProvider;
        try{
            deliveryProvider = deliverySelectRepository.findDeliveryByDesId(desId).get(0);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }
        return deliveryProvider;
    }

    /**
     * 해당 회원의 존재하는 배송지 조
     *
     */
    public List<Integer> retrieveExistDeliveryDestination(long userId) throws BaseException{
        List<Integer> isMainList;
        try{
            isMainList = deliverySelectRepository.findIsMainByUserId(userId);
        }catch(Exception e){
            isMainList = null;
        }
        return isMainList;
    }

    /**
     * desId를 통해서 정보 가져오기
     * for purchase response
     */
    public GetDelivery retrieveDelivery(long desId) throws BaseException{
        GetDelivery getDelivery;
        try{
            getDelivery = deliverySelectRepository.findDeliveryInfoByDesId(desId).get(0);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }
        return getDelivery;
    }

    /**
     * 기본배송지 모든 정보 가져오기
     * --> for 기본 배송지 변경
     */
    public GetDelivery retrieveMainDeliveryInfo(long userId) throws BaseException{
        GetDelivery getDelivery;
        try{
            getDelivery = deliverySelectRepository.findMainDeliveryInfoByDesId(userId).get(0);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }
        return getDelivery;
    }





    public List<GetDeliveryRes> changedDelToRes(List<GetDelivery> list){
        List<GetDeliveryRes> changedList;
        changedList = list.stream().map( GetUserDelivery -> {
            String userName = GetUserDelivery.getUserName();
            String mainAdress = GetUserDelivery.getMainAddress();
            String subAdress = GetUserDelivery.getSubAddress();
            String phoneNum = GetUserDelivery.getPhoneNum();
            boolean isMain = (GetUserDelivery.getIsMain() ==1)? true : false;
            long desId = GetUserDelivery.getDesId();

            return new GetDeliveryRes(userName,mainAdress,subAdress,phoneNum,isMain,desId);

        }).collect(Collectors.toList());

        return changedList;
    }

    public GetMainDeliveryRes changeMainDelToRes(GetMainDelivery main){
        GetMainDeliveryRes result;

          String   nameNum = main.getUserName() + "  " + main.getPhoneNum();
          String   mainAddress = main.getMainAddress();
           String  subAddress = main.getSubAddress();
           long desId = main.getMainDel();



        result = new GetMainDeliveryRes(nameNum,mainAddress,subAddress,desId);
        return result;
    }
}
