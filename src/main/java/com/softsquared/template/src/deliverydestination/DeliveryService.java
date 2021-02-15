package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.DBmodel.DeliveryDestination;
import com.softsquared.template.config.*;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.deliverydestination.model.DeleteDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.softsquared.template.config.BaseResponseStatus.FAILED_TO_GET_DELVIERY_DESTINATION;

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
    public boolean deleteDeliveryDestination(long desId) throws BaseException{
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

        deliveryRepository.save(newDes);

        return true;


    }

}
