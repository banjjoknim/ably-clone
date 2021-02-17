package com.softsquared.template.src.deliverydestination;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.softsquared.template.DBmodel.DeliveryDestination;
import com.softsquared.template.DBmodel.QUserInfo;
import com.softsquared.template.config.*;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.deliverydestination.model.DeleteDelivery;
import com.softsquared.template.src.deliverydestination.model.GetDelivery;
import com.softsquared.template.src.deliverydestination.model.PatchDeliveryReq;
import com.softsquared.template.src.deliverydestination.model.PostDeliveryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class DeliveryService {
    private final DeliveryProvider deliveryProvider;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryProvider deliveryProvider , DeliveryRepository deliveryRepository){
        this.deliveryProvider = deliveryProvider;
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * 배송지 삭제하기
     */
    public long deleteDeliveryDestination(long desId) throws BaseException{
        DeleteDelivery deleteDelivery;
        DeliveryDestination newDes;

        try{
            deleteDelivery = deliveryProvider.retrieveDeleteDelivery(desId);

        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }
        long userId = deleteDelivery.getUserId();
        String detailAddress = deleteDelivery.getDeltailAddress();
        String phoneNUm = deleteDelivery.getPhoneNum();
        String userName =deleteDelivery.getUserName();
        String address = deleteDelivery.getAddress();
        String dateUpdated = deleteDelivery.getDateUpdated();
        String dateCreated = deleteDelivery.getDateCreated();

        newDes = new DeliveryDestination(desId,userId,detailAddress,
                phoneNUm,userName,
                address,1,dateUpdated,dateCreated,0);

        try {
            deliveryRepository.save(newDes);
        }catch (Exception e){
            throw new BaseException(FAILED_TO_DELETE_DELIVRY);
        }

        return userId;


    }

    /**
     * 배송지 추가
     */
    public Integer createDeliveryDestination(long userId, PostDeliveryReq param) throws BaseException{
        List<Integer> isMainList;
        try{
            isMainList = deliveryProvider.retrieveExistDeliveryDestination(userId);
        }catch (Exception e){
            e.printStackTrace();
            isMainList = null;
        }
        long desuserId= userId;
        String detailAddress = param.getDetailAddress();
        String phoneNum = param.getPhoneNum();
        String userName = param.getName();
        String address = param.getAddress();
        String dateCreated = (new Timestamp(System.currentTimeMillis())).toString();
        String dateUpdated = "0000-00-00 00:00:00";
        int status =0;
        int isMain=0;
        boolean hasMain=false;

        if(isMainList == null || isMainList.size()==0){
            isMain = 0;
        }
        else{
            for(int i=0;i<isMainList.size();i++){
                if(isMainList.get(i) == 1) {
                    isMain = 0;
                    hasMain = true;
                    break;
                }
            }
            if(hasMain == false)
                isMain = 1;
        }
        System.out.println("here");

        DeliveryDestination deliveryDestination = new DeliveryDestination(
                userId,detailAddress, phoneNum,userName,address,0,dateUpdated,dateCreated,isMain);

        deliveryRepository.save(deliveryDestination);


        return isMain;
    }

    public int modifyDeliveryDestination(PatchDeliveryReq param,long userId) throws BaseException{

        String detailAddress = param.getDetailAddress();
        String phoneNum = param.getPhoneNum();
        String userName = param.getName();
        String address = param.getAddress();
        String dateCreated = (new Timestamp(System.currentTimeMillis())).toString();
        String dateUpdated = (new Timestamp(System.currentTimeMillis())).toString();
        GetDelivery getDelivery;
        System.out.println(param.getDesId()+"?????");
        try{
            getDelivery = deliveryProvider.retrieveDelivery(param.getDesId());
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }
        int isMain = getDelivery.getIsMain();
        try {
            DeliveryDestination deliveryDestination = new DeliveryDestination(param.getDesId(),
                    userId, detailAddress, phoneNum, userName, address, 0, dateUpdated, dateCreated, isMain);
            deliveryRepository.save(deliveryDestination);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_PATCH_DELIVRY);
        }
        return isMain;
    }

}
