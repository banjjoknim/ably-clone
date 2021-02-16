package com.softsquared.template.src.purchase;

import com.softsquared.template.DBmodel.ProductImage;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.Caster;
import com.softsquared.template.src.deliverydestination.DeliveryProvider;
import com.softsquared.template.src.deliverydestination.model.GetMainDeliveryRes;
import com.softsquared.template.src.product.ProductProvider;
import com.softsquared.template.src.product.ProductsProvider;
import com.softsquared.template.src.purchase.model.*;
import com.softsquared.template.src.user.UserInfoProvider;
import com.softsquared.template.src.user.models.GetUserRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class PurchaseProvider {
    private final PurchaseSelectRepository purchaseSelectRepository;
    private final UserInfoProvider userInfoProvider;
    private final DeliveryProvider deliveryProvider;
    private final ProductsProvider productsProvider;
    private final Caster caster;

    @Lazy
    @Autowired
    public PurchaseProvider(PurchaseSelectRepository purchaseSelectRepository, UserInfoProvider userInfoProvider,
                            DeliveryProvider deliveryProvider,ProductsProvider productsProvider){
        this.purchaseSelectRepository =purchaseSelectRepository;
        this.userInfoProvider = userInfoProvider;
        this.deliveryProvider = deliveryProvider;
        this.productsProvider = productsProvider;
        this.caster = new Caster();

    }

    /**
     * 구매하기의 구매 상품 조회
     */
    public GetPurchaseProductRes retrievePurchaseProduct(long productId, List<String> options, List<Integer> num) throws BaseException{
        int totalNum=0;
        List<GetPurchaseProductCasted> catedList = new ArrayList<>();
        GetPurchaseProduct getPurchaseProduct;
        try{
            getPurchaseProduct = productsProvider.retrieveProductWithProductId(productId);
        }catch (Exception e){
            throw new BaseException(FAILED_TO_GET_PRODUCT);
        }
        String img = getPurchaseProduct.getImg();
        String marketName = getPurchaseProduct.getMarketName();
        String productName = getPurchaseProduct.getProductName();
        if(productName.length()>=20){
            productName.substring(0,21);
            productName+="...";
        }
        int realPrice = getPurchaseProduct.getPrice();
        int discount = getPurchaseProduct.getDiscount();
        int price = (int)(realPrice*(1-discount*0.01));

        for(int i=0;i<options.size();i++){

            //해당 제품에 대한 정보 가져오기

            int count =num.get(i);
            String countStr = count+"개";
            int totalPrice = price*count;

            String option = options.get(i);

            totalNum+=count;

            GetPurchaseProductCasted cated = new GetPurchaseProductCasted(img,marketName,productName,totalPrice,countStr,option);

            catedList.add(cated);

        }
        String totalNumStr = "주문 상품 총"+totalNum+"개";
        String marketNameStr = marketName+" 배송상품 " +totalNum+"개";


        GetPurchaseProductRes productRes = new GetPurchaseProductRes(totalNumStr,marketNameStr,catedList,price*totalNum,
                realPrice*totalNum,(-1)*(realPrice-price)*totalNum,0);
        return productRes;
    }

    /**
     * 단순 제품 정보 조회
     * service 에서 사용
     */
    public GetPurchaseProduct retrieveProductInfo(long productId) throws BaseException{

        GetPurchaseProduct getPurchaseProduct;
        try{
            getPurchaseProduct = productsProvider.retrieveProductWithProductId(productId);
        }catch (Exception e){
            throw new BaseException(FAILED_TO_GET_PRODUCT);
        }

        return getPurchaseProduct;
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
    /**
     * 마지막 id 가져오기
     *
     */
    public long retrieveLastId() throws BaseException{
        List<Long> purIdList;
        try{
            purIdList = purchaseSelectRepository.findPurIdList();
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PURCHASE);
        }
        return purIdList.get(purIdList.size()-1);
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
