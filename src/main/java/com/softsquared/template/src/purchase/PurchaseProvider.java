package com.softsquared.template.src.purchase;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.deliverydestination.DeliveryProvider;
import com.softsquared.template.src.deliverydestination.model.GetMainDeliveryRes;
import com.softsquared.template.src.purchase.model.GetPurchaseRefundRes;
import com.softsquared.template.src.user.UserInfoProvider;
import com.softsquared.template.src.user.models.GetUserRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class PurchaseProvider {
    private final PurchaseSelectRepository purchaseSelectRepository;
    private final UserInfoProvider userInfoProvider;
    private final DeliveryProvider deliveryProvider;

    @Lazy
    @Autowired
    public PurchaseProvider(PurchaseSelectRepository purchaseSelectRepository, UserInfoProvider userInfoProvider,
                            DeliveryProvider deliveryProvider){
        this.purchaseSelectRepository =purchaseSelectRepository;
        this.userInfoProvider = userInfoProvider;
        this.deliveryProvider = deliveryProvider;

    }


    public GetPurchaseRefundRes retrievePurchaseRefundInfo(long userId) throws BaseException{
        GetUserRefund userRefund;
        try{
            userRefund =userInfoProvider.retrieveRefundInfo(userId);

        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_USER);
        }

        if(userRefund.getName()==null)
            throw new BaseException(SUCCESS_CHECK_REFUND_INFO);

        return changeToPurchaseRefundRes(userRefund);

    }

    public GetMainDeliveryRes retrievePurchaseMainAddress(long userId) throws BaseException{
        GetMainDeliveryRes mainDeliveryRes;
        try{
            mainDeliveryRes = deliveryProvider.retrieveMainDelivery(userId);
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(SUCCESS_CHECK_DELIVERY_DESTINATION);
        }
        return mainDeliveryRes;
    }

    public long retrievePurchaseCount(long userId) throws BaseException{
        long purchaseCount;
        try{
            purchaseCount = purchaseSelectRepository.findPurchaseCountByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PURCHASE);
        }
        return purchaseCount;
    }

    public GetPurchaseRefundRes changeToPurchaseRefundRes(GetUserRefund userRefund){
        GetPurchaseRefundRes getPurchaseRefund;
        String result = userRefund.getName() + " | "+ userRefund.getBank()+ " "+userRefund.getAccount();

        if(userRefund.getName() == null)
            result = "환불받을 계좌를 입력해주세요";

        getPurchaseRefund = new GetPurchaseRefundRes(result);


        return getPurchaseRefund;
    }
}
