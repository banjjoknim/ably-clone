package com.softsquared.template.src.purchase;

import com.softsquared.template.DBmodel.Purchase;
import com.softsquared.template.DBmodel.PurchaseProduct;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.deliverydestination.DeliveryProvider;
import com.softsquared.template.src.deliverydestination.model.GetDelivery;
import com.softsquared.template.src.purchase.model.GetPurchaseProduct;
import com.softsquared.template.src.purchase.model.GetPurchaseProductRes;
import com.softsquared.template.src.purchase.model.PostPurchaseReq;
import com.softsquared.template.src.purchase.model.PostPurchaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProvider purchaseProvider;
    private final PurchaseProductRepository purchaseProductRepository;
    private final DeliveryProvider deliveryProvider;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseProvider purchaseProvider,
                           PurchaseProductRepository purchaseProductRepository,DeliveryProvider deliveryProvider){
        this.purchaseRepository = purchaseRepository;
        this.purchaseProvider = purchaseProvider;
        this.purchaseProductRepository = purchaseProductRepository;
        this.deliveryProvider = deliveryProvider;

    }

    /**
     * 구매하기
     */
    public PostPurchaseRes createPurchase(PostPurchaseReq param, long userId) throws BaseException{

        Purchase newPurchase;
        int coupon = 0;
        int point = 0;
        int price = param.getTotalPrice();
        long desId =param.getDesId();
        long paymentCode = param.getPaymentCode();
        int status = 0;
        long purchaseUserId = userId;
        long purProductCode = param.getProductId();
        long purId;

        //마지막 purId 알기
        try{
            purId = purchaseProvider.retrieveLastId()+1;
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PURCHASE);
        }

        newPurchase = new Purchase(purId,coupon,point,price,desId,paymentCode,purProductCode,status,purchaseUserId);

        purchaseRepository.save(newPurchase);

        //이 제품에 대한 정보 가져오기
        GetPurchaseProduct getPurchaseProduct;
        try{
            getPurchaseProduct = purchaseProvider.retrieveProductInfo(param.getProductId());
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PRODUCT);
        }
        int realPrice = getPurchaseProduct.getPrice();
        int discount = getPurchaseProduct.getDiscount();
        int disprice = (int)(realPrice*(1-discount*0.01));

        //purchaseProductRepository 생성
        for(int i=0;i<param.getOptions().size();i++){
            PurchaseProduct purchaseProduct;
            long purchaseId = purId;
            long productId = param.getProductId();
            String options = param.getOptions().get(i);
            int count = param.getNum().get(i);
            int finalprice = count*disprice;

            int productStatus = 0;
            char purStatus = 'R';

            purchaseProduct = new PurchaseProduct(purchaseId,productId,finalprice,options,
                    count,productStatus,purStatus);

            purchaseProductRepository.save(purchaseProduct);

        }
        GetDelivery getDelivery;
        try{
            getDelivery = deliveryProvider.retrieveDelivery(desId);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_DELVIERY_DESTINATION);
        }

        String name = getDelivery.getUserName();
        String address = getDelivery.getMainAddress()+" "+getDelivery.getSubAddress();
        String phoneNum = getDelivery.getPhoneNum();

        PostPurchaseRes postPurchaseRes= new PostPurchaseRes(purId,name,address,phoneNum);

        return postPurchaseRes;
    }
}
