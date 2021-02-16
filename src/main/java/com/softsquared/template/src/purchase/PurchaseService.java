package com.softsquared.template.src.purchase;

import com.softsquared.template.DBmodel.Purchase;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.purchase.model.PostPurchaseReq;
import com.softsquared.template.src.purchase.model.PostPurchaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProvider purchaseProvider;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseProvider purchaseProvider){
        this.purchaseRepository = purchaseRepository;
        this.purchaseProvider = purchaseProvider;

    }

//    /**
//     * 구매하기
//     */
//    public PostPurchaseRes createPurchase(PostPurchaseReq param, long userId) throws BaseException{
//        Purchase newPurchase;
//
//    }
}
